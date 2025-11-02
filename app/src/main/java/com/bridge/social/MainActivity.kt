package com.bridge.social

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bridge.social.onboarding.OnboardingAdapter
import com.bridge.social.onboarding.OnboardingItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if onboarding has been completed
        val prefs = getSharedPreferences("bridge_prefs", MODE_PRIVATE)
        if (prefs.getBoolean("onboarding_complete", false)) {
            // Skip to home activity
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)
        setupOnboarding()
    }

    private fun setupOnboarding() {
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        val onboardingItems = listOf(
            // Screen 1: Welcome with name input
            OnboardingItem(
                title = "Welcome to Bridge",
                subtitle = "Let's get to know you better",
                buttonText = null,
                showNameInput = true
            ),

            // Screen 2: Goals selection
            OnboardingItem(
                title = "What are your goals?",
                subtitle = "Select what brings you to Bridge",
                buttonText = null,
                showGoalsSelection = true
            ),

            // Screen 3: Gender selection
            OnboardingItem(
                title = "Which gender best describes you?",
                subtitle = "This helps us personalize your experience",
                buttonText = null,
                showGenderSelection = true
            ),

            // Screen 4: Nationality
            OnboardingItem(
                title = "What's your nationality?",
                subtitle = "Connect with people from around the world",
                buttonText = null,
                showNationalitySelection = true
            ),

            // Screen 5: Email (no verification code)
            OnboardingItem(
                title = "What's your email?",
                subtitle = "We'll use this to save your profile",
                buttonText = null,
                showEmailInput = true,
                showVerificationCode = false // Changed to false
            ),

            // Screen 6: Personality Quiz Q1
            OnboardingItem(
                title = "In your free time, you usually...",
                subtitle = "Help us understand your personality",
                buttonText = null,
                showPersonalityQuiz = true
            ),

            // Screen 7: Social gathering question
            OnboardingItem(
                title = "At social gatherings, you're usually...",
                subtitle = "How do you interact with others?",
                buttonText = null,
                showSocialQuestion = true
            ),

            // Screen 8: Meeting people question
            OnboardingItem(
                title = "When meeting new people...",
                subtitle = "How do you connect?",
                buttonText = null,
                showMeetingQuestion = true
            ),

            // Screen 9: Project question
            OnboardingItem(
                title = "In a new project or event...",
                subtitle = "What's your role?",
                buttonText = null,
                showProjectQuestion = true
            ),

            // Screen 10: Interests selection
            OnboardingItem(
                title = "What are your interests?",
                subtitle = "Select at least 3 to find like-minded people",
                buttonText = null,
                showInterestsSelection = true
            ),

            // Screen 11: Connection goal
            OnboardingItem(
                title = "What's your connection goal?",
                subtitle = "Be specific about what you're looking for",
                buttonText = null,
                showConnectionGoal = true
            ),

            // Screen 12: Skills
            OnboardingItem(
                title = "What skills can you share?",
                subtitle = "Help others while you connect",
                buttonText = null,
                showSkillsInput = true
            ),

            // Screen 13: Bio
            OnboardingItem(
                title = "Describe yourself",
                subtitle = "Make a memorable first impression",
                buttonText = null,
                showBioInput = true
            ),

            // Screen 14: Location
            OnboardingItem(
                title = "Where are you located?",
                subtitle = "Find people near you",
                buttonText = null,
                showLocationInput = true
            )
        )

        val adapter = OnboardingAdapter(this, onboardingItems) { isLastPage ->
            if (isLastPage) {
                // Mark onboarding as complete and go to home
                val prefs = getSharedPreferences("bridge_prefs", MODE_PRIVATE)
                prefs.edit().putBoolean("onboarding_complete", true).apply()

                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                // Move to next page
                val currentItem = viewPager.currentItem
                if (currentItem < onboardingItems.size - 1) {
                    viewPager.currentItem = currentItem + 1
                }
            }
        }

        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false // Disable swipe to force button usage

        TabLayoutMediator(tabLayout, viewPager) { _, _ ->
            // Empty implementation - dots are handled by tab selector drawable
        }.attach()
    }
}