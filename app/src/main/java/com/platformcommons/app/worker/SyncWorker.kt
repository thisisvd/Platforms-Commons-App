package com.platformcommons.app.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.platformcommons.app.data.local.UserDao
import com.platformcommons.app.utils.NetworkUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import javax.inject.Inject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val dao: UserDao,
    private val networkUtils: NetworkUtils
) : Worker(context, params) {

    override fun doWork(): Result {

        if (!networkUtils.isNetworkAvailable()) {
            return Result.retry()
        }

        return try {

            Timber.d("Worker started working!")

//            val unSyncData = myDataDao.getUnsyncedData()
//
//            val success = syncWithServer(unsyncedData)
//
//            if (success) {
//                // Mark the data as synced in Room
//                myDataDao.markDataAsSynced(unsyncedData)
//            }

            Result.success()
        } catch (e: Exception) {
            Timber.d(e.message)
            Result.retry()
        }
    }

//    private fun syncWithServer(data: List<MyData>): Boolean {
//        // Simulate a network call to sync with the server
//        // Replace with actual network syncing logic
//        return true
//    }
}
