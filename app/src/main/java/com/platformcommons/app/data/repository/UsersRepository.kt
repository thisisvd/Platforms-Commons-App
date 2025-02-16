package com.platformcommons.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.platformcommons.app.data.local.UserDao
import com.platformcommons.app.data.local.model.User
import com.platformcommons.app.data.network.users.UsersApiImpl
import com.platformcommons.app.domain.users.Data
import com.platformcommons.app.domain.users.NewUserResponse
import com.platformcommons.app.ui.users.pagination.UsersPagingSource
import com.platformcommons.app.ui.users.pagination.UsersPagingSource.Companion.USER_LIST_TOTAL_PAGES
import com.platformcommons.app.core.NetworkUtils
import com.platformcommons.app.core.events.RxBus
import com.platformcommons.app.core.events.RxEvents
import com.platformcommons.app.worker.SyncWorker
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

    suspend fun addUser(user: User): Response<NewUserResponse> {
        return if (networkUtils.isNetworkAvailable()) {
            try {
                val response = apiImpl.addUser(user)
                response
            } catch (exception: Exception) {
                saveToLocalDatabase(user)
                Response.error(
                    500, "Internal Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
                )
            }
        } else {
            saveToLocalDatabase(user)
            Response.error(
                500,
                "No network available, data stored locally".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }

    private suspend fun saveToLocalDatabase(user: User) {
        val result = dao.insertUser(user)
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
                        // sending on sync event
                        RxBus.publish(RxEvents.OnSyncCompleteEvent(true))
                        Timber.d("SyncWorker: Sync completed successfully")
                    }

                    WorkInfo.State.FAILED -> {
                        RxBus.publish(RxEvents.OnSyncCompleteEvent(false))
                        Timber.d("SyncWorker: Sync failed: ${workInfo.outputData.getString("error_message")}")
                    }

                    WorkInfo.State.RUNNING -> {
                        Timber.d("SyncWorker: Sync is in progress")
                    }

                    else -> {
                        Timber.d("SyncWorker: Work ENQUEUED")
                    }
                }
            }
        }
    }
}