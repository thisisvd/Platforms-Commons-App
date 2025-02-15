package com.platformcommons.app.model.users

data class NewUserResponse(
    val createdAt: String,
    val id: String,
    val job: String,
    val name: String
)