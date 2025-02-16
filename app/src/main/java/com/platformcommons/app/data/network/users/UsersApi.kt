package com.platformcommons.app.data.network.users

import com.platformcommons.app.data.local.model.User
import com.platformcommons.app.domain.users.NewUserResponse
import com.platformcommons.app.domain.users.UsersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UsersApi {

    @GET("users")
    suspend fun getUsers(
        @Query("page") pageNumber: Int = 1
    ): Response<UsersResponse>

    @POST("users")
    suspend fun addUser(
        @Body request: User
    ): Response<NewUserResponse>
}