package com.bridge.social.model

data class User(
    val id: Long = 0,
    val name: String,
    val email: String,
    val goals: List<String> = emptyList(),
    val interests: List<String> = emptyList(),
    val bio: String = "",
    val location: String = "",
    val isFakeUser: Boolean = false
)