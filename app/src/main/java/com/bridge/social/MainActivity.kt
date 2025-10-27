package com.bridge.social

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bridge.social.model.User
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val users = mutableListOf<User>()
    private val fakeUsers = mutableListOf<User>()
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        createInitialFakeUsers()
    }

    private fun setupViews() {
        findViewById<Button>(R.id.btnStartMatching).setOnClickListener {
            startMatchingProcess()
        }

        findViewById<Button>(R.id.btnQuickMatch).setOnClickListener {
            showQuickMatches()
        }

        findViewById<FloatingActionButton>(R.id.fabAddFakeUser).setOnClickListener {
            createFakeUser()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rvUsers)
        adapter = UserAdapter(fakeUsers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        updateStats()
    }

    private fun createInitialFakeUsers() {
        val initialUsers = listOf(
            User(1, "Emma Wilson", "emma@example.com",
                listOf("Networking", "Learn skills"),
                listOf("Tech", "Music", "Travel"),
                "Love meeting new people!", "San Francisco", true),
            User(2, "James Chen", "james@example.com",
                listOf("Make friends", "Share knowledge"),
                listOf("Gaming", "Sports", "Cooking"),
                "Enthusiastic about life", "New York", true),
            User(3, "Sophia Martinez", "sophia@example.com",
                listOf("Career growth", "Mentorship"),
                listOf("Art", "Reading", "Yoga"),
                "Always learning", "London", true)
        )
        fakeUsers.addAll(initialUsers)
        adapter.notifyDataSetChanged()
        updateStats()
    }

    private fun startMatchingProcess() {
        if (fakeUsers.size < 3) {
            Toast.makeText(this, "Need at least 3 fake users to start matching!", Toast.LENGTH_LONG).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Matching Process")
            .setMessage("Ready to join the queue!\n\n" +
                    "How it works:\n" +
                    "1. You'll be placed in a queue\n" +
                    "2. You'll select 1 from 3 profiles (no photos)\n" +
                    "3. Selected person picks next\n" +
                    "4. Process continues until 4 people matched\n" +
                    "5. Photos revealed after group complete!")
            .setPositiveButton("Join Queue") { _, _ ->
                simulateMatching()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun simulateMatching() {
        // Show first selection
        val candidates = fakeUsers.shuffled().take(3)
        val message = buildString {
            appendLine("Your turn to select!")
            appendLine("\nChoose 1 person from these 3:\n")
            candidates.forEachIndexed { index, user ->
                appendLine("${index + 1}. Goals: ${user.goals.joinToString(", ")}")
                appendLine("   Interests: ${user.interests.joinToString(", ")}")
                appendLine("   Location: ${user.location}\n")
            }
        }

        AlertDialog.Builder(this)
            .setTitle("Select Your Match")
            .setMessage(message)
            .setPositiveButton("Select #1") { _, _ ->
                completeMatching(candidates[0])
            }
            .setNegativeButton("Select #2") { _, _ ->
                completeMatching(candidates[1])
            }
            .setNeutralButton("Select #3") { _, _ ->
                completeMatching(candidates[2])
            }
            .show()
    }

    private fun completeMatching(selected: User) {
        val finalGroup = mutableListOf(selected)
        finalGroup.addAll(fakeUsers.shuffled().filter { it.id != selected.id }.take(2))
        finalGroup.add(User(99, "You", "you@example.com",
            listOf("Your goals"), listOf("Your interests"), "Your bio", "Your location", false))

        val message = buildString {
            appendLine("Match Complete! Your group of 4:\n")
            finalGroup.forEach { user ->
                appendLine("• ${user.name}")
                appendLine("  ${user.bio}")
                appendLine("  Location: ${user.location}\n")
            }
        }

        AlertDialog.Builder(this)
            .setTitle("Group Formed!")
            .setMessage(message)
            .setPositiveButton("Schedule Meeting", null)
            .setNegativeButton("Close", null)
            .show()
    }

    private fun showQuickMatches() {
        val message = buildString {
            appendLine("Top 3 Matches for You:\n")
            fakeUsers.take(3).forEach { user ->
                appendLine("• ${user.name}")
                appendLine("  Interests: ${user.interests.joinToString(", ")}")
                appendLine("  Goals: ${user.goals.joinToString(", ")}\n")
            }
        }

        AlertDialog.Builder(this)
            .setTitle("Quick Match Results")
            .setMessage(message)
            .setPositiveButton("Start Matching", null)
            .setNegativeButton("Close", null)
            .show()
    }

    private fun createFakeUser() {
        val newId = (fakeUsers.maxOfOrNull { it.id } ?: 0) + 1
        val names = listOf("Alex", "Morgan", "Taylor", "Jordan", "Casey")
        val goals = listOf(
            listOf("Networking", "Learn skills"),
            listOf("Make friends", "Find mentors"),
            listOf("Share knowledge", "Build community")
        )
        val interests = listOf(
            listOf("Tech", "Gaming", "Music"),
            listOf("Sports", "Travel", "Food"),
            listOf("Art", "Reading", "Movies")
        )
        val locations = listOf("Boston", "Seattle", "Austin", "Denver", "Miami")

        val randomUser = User(
            id = newId,
            name = "${names.random()} Test",
            email = "test$newId@example.com",
            goals = goals.random(),
            interests = interests.random(),
            bio = "Auto-generated test user #$newId",
            location = locations.random(),
            isFakeUser = true
        )

        fakeUsers.add(randomUser)
        adapter.notifyItemInserted(fakeUsers.size - 1)
        updateStats()

        Toast.makeText(this, "Created: ${randomUser.name}", Toast.LENGTH_SHORT).show()
    }

    private fun updateStats() {
        findViewById<TextView>(R.id.tvStats).text =
            "Fake Users: ${fakeUsers.size} | Ready to Match: ${fakeUsers.size >= 3}"
    }
}

// Simple adapter for RecyclerView
class UserAdapter(private val users: List<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.textView.text = "${user.name} - ${user.interests.joinToString(", ")}"
    }

    override fun getItemCount() = users.size
}