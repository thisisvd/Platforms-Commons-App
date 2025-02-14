package com.platformcommons.app.api

import com.platformcommons.app.model.AddUser
import com.platformcommons.app.model.AddUserResponse
import com.platformcommons.app.model.UsersResponse
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

    @POST
    suspend fun addUser(
        @Body request: AddUser
    ): Response<AddUserResponse>
}