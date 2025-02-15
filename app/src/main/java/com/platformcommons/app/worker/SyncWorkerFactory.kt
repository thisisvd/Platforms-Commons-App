package com.platformcommons.app.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.platformcommons.app.data.local.UserDao
import com.platformcommons.app.utils.NetworkUtils
import timber.log.Timber
import javax.inject.Inject

class SyncWorkerFactory @Inject constructor(
    private val dao: UserDao, private val networkUtils: NetworkUtils
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context, workerClassName: String, workerParameters: WorkerParameters
    ): ListenableWorker {
        Timber.d("CreateWorker Reached!")
        return SyncWorker(appContext, workerParameters, dao, networkUtils)
    }
}