package com.platformcommons.app.ui.users.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.platformcommons.app.data.local.UserDao
import com.platformcommons.app.data.local.User
import com.platformcommons.app.network.users.UsersApiImpl
import com.platformcommons.app.model.users.NewUser
import com.platformcommons.app.model.users.Data
import com.platformcommons.app.model.users.NewUserResponse
import com.platformcommons.app.ui.users.pagination.UsersPagingSource
import com.platformcommons.app.ui.users.pagination.UsersPagingSource.Companion.USER_LIST_TOTAL_PAGES
import com.platformcommons.app.utils.NetworkUtils
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val apiImpl: UsersApiImpl,
    private val networkUtils: NetworkUtils,
    private val dao: UserDao
) {

    fun getUsers(): Flow<PagingData<Data>> {
        return Pager(config = PagingConfig(
            pageSize = USER_LIST_TOTAL_PAGES, enablePlaceholders = false
        ), pagingSourceFactory = { UsersPagingSource(apiImpl) }).flow
    }

    suspend fun addUser(request: NewUser): Response<NewUserResponse> {
        return if (networkUtils.isNetworkAvailable()) {
            try {
                val response = apiImpl.addUser(request)
                response
            } catch (exception: Exception) {
                saveToLocalDatabase(request)
                Response.error(
                    500, "Internal Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            }
        } else {
            saveToLocalDatabase(request)
            Response.error(
                500,
                "No network available, data stored locally".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    private suspend fun saveToLocalDatabase(request: NewUser) {
        Timber.d("SAVE IN DB ${Gson().toJson(request)}")
        dao.insertUser(User(name = request.name, job = request.job))
    }
}