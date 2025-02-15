package com.platformcommons.app.network.users

import com.platformcommons.app.model.users.NewUser
import com.platformcommons.app.model.users.NewUserResponse
import com.platformcommons.app.model.users.UsersResponse
import retrofit2.Response
import javax.inject.Inject

class UsersApiImpl @Inject constructor(
    private val api: UsersApi
) : UsersApiHelper {

    override suspend fun getUsers(page: Int): Response<UsersResponse> = api.getUsers(page)

    override suspend fun addUser(request: NewUser): Response<NewUserResponse> = api.addUser(request)
}