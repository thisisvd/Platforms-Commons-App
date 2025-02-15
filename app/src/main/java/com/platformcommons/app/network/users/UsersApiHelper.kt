package com.platformcommons.app.network.users

import com.platformcommons.app.data.local.User
import com.platformcommons.app.model.users.NewUserResponse
import com.platformcommons.app.model.users.UsersResponse
import retrofit2.Response

interface UsersApiHelper {

    suspend fun getUsers(page: Int): Response<UsersResponse>

    suspend fun addUser(user: User): Response<NewUserResponse>
}