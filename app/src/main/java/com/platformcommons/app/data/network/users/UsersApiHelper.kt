package com.platformcommons.app.data.network.users

import com.platformcommons.app.data.local.model.User
import com.platformcommons.app.domain.users.NewUserResponse
import com.platformcommons.app.domain.users.UsersResponse
import retrofit2.Response

interface UsersApiHelper {

    suspend fun getUsers(page: Int): Response<UsersResponse>

    suspend fun addUser(user: User): Response<NewUserResponse>
}