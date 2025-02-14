package com.platformcommons.app.api

import com.platformcommons.app.model.AddUser
import com.platformcommons.app.model.AddUserResponse
import com.platformcommons.app.model.UsersResponse
import retrofit2.Response
import javax.inject.Inject

class UsersApiImpl @Inject constructor(
    private val api: UsersApi
) : UsersApiHelper {

    override suspend fun getUsers(page: Int): Response<UsersResponse> = api.getUsers(page)

    override suspend fun addUser(request: AddUser): Response<AddUserResponse> = api.addUser(request)
}