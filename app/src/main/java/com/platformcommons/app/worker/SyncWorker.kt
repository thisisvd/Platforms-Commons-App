package com.platformcommons.app.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.platformcommons.app.data.local.UserDao
import com.platformcommons.app.network.users.UsersApiImpl
import com.platformcommons.app.utils.NetworkUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val dao: UserDao,
    private val networkUtils: NetworkUtils,
    private val apiImpl: UsersApiImpl
) : Worker(context, params) {

    override fun doWork(): Result {

        if (!networkUtils.isNetworkAvailable()) {
            return Result.retry()
        }

        return try {

            Timber.d("Worker STARTED WORKING!")

            CoroutineScope(Dispatchers.IO).launch {
                val result = userSyncing()

                if (!result) {
                    Result.retry()
                }
            }

            Result.success()
        } catch (e: Exception) {
            Timber.d(e.message)
            Result.retry()
        }
    }

    private suspend fun userSyncing(): Boolean {

        var syncingSuccess = true

        dao.getAllUsers().collect { unSyncedUsers ->
            if (networkUtils.isNetworkAvailable()) {
                unSyncedUsers.forEach { user ->
                    try {
                        val response = apiImpl.addUser(user)
                        if (response.isSuccessful) {
                            response.body()?.let {
                                dao.deleteUser(it.userId)
                            }
                        }
                    } catch (exception: Exception) {
                        syncingSuccess = false
                    }
                }
            }
        }

        return syncingSuccess
    }
}
