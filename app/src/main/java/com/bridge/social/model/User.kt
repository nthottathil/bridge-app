package com.bridge.social.model

import java.io.Serializable

data class User(
    val id: Long = 0,
    val name: String,
    val email: String,
    val goals: List<String> = emptyList(),
    val interests: List<String> = emptyList(),
    val bio: String = "",
    val location: String = "",
    val isFakeUser: Boolean = false,
    val personality: String = "",
    val skills: List<String> = emptyList(),
    val gender: String = "",
    val nationality: String = "",
    val profileImageUrl: String? = null,
    val connectionGoal: String = "",
    val matchScore: Int = 0
) : Serializable
