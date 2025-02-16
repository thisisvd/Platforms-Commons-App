package com.platformcommons.app.data.network.users

import com.platformcommons.app.data.local.model.User
import com.platformcommons.app.domain.users.NewUserResponse
import com.platformcommons.app.domain.users.UsersResponse
import retrofit2.Response
import javax.inject.Inject

class UsersApiImpl @Inject constructor(
    private val api: UsersApi
) : UsersApiHelper {

    override suspend fun getUsers(page: Int): Response<UsersResponse> = api.getUsers(page)

    override suspend fun addUser(user: User): Response<NewUserResponse> = api.addUser(user)
}