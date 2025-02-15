package com.platformcommons.app.ui.users.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
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
import com.platformcommons.app.worker.SyncWorker
import com.platformcommons.app.worker.SyncWorkerFactory
import kotlinx.coroutines.Delay
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
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
    private val dao: UserDao,
    private val workManager: WorkManager
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
        val result = dao.insertUser(User(name = request.name, job = request.job))
        if (result > 0) {
            syncData()
        }
    }

    private fun syncData() {
        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        workManager.enqueue(syncRequest)

        // Observe work status
        workManager.getWorkInfoByIdLiveData(syncRequest.id).observeForever { workInfo ->
            if (workInfo != null) {
                when (workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        // Handle success (e.g., show a message or update UI)
                        Timber.d("SyncWorker: Sync completed successfully")
                    }
                    WorkInfo.State.FAILED -> {
                        // Handle failure (e.g., show an error message or retry)
                        Timber.d("SyncWorker: Sync failed: ${workInfo.outputData.getString("error_message")}")
                    }
                    WorkInfo.State.RUNNING -> {
                        // Sync is in progress
                        Timber.d("SyncWorker: Sync is in progress")
                    }
                    else -> {
                        // Other states like CANCELED or ENQUEUED
                        Timber.d("SyncWorker: ELSE BLOCK")
                    }
                }
            }
        }
    }
}