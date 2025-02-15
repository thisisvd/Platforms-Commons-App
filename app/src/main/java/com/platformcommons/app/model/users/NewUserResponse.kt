package com.platformcommons.app.model.users

data class NewUserResponse(
    val createdAt: String,
    val id: String,
    val userId: Int,
    val job: String,
    val name: String,
    val synced: Boolean = false
)