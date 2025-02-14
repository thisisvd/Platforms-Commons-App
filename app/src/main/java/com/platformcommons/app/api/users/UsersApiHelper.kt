package com.platformcommons.app.api.users

import com.platformcommons.app.model.AddUser
import com.platformcommons.app.model.AddUserResponse
import com.platformcommons.app.model.UsersResponse
import retrofit2.Response

interface UsersApiHelper {

    suspend fun getUsers(page: Int): Response<UsersResponse>

    suspend fun addUser(request: AddUser): Response<AddUserResponse>
}