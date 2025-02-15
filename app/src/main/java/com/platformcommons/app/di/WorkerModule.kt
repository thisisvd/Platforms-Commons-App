package com.platformcommons.app.di

import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.platformcommons.app.data.local.UserDao
import com.platformcommons.app.utils.NetworkUtils
import com.platformcommons.app.worker.SyncWorker
import com.platformcommons.app.worker.SyncWorkerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {

    @Provides
    @Singleton
    fun provideWorkerFactory(
        dao: UserDao, networkUtils: NetworkUtils
    ): WorkerFactory {
        return SyncWorkerFactory(dao, networkUtils)
    }

    @Provides
    fun provideWorkManager(
        @ApplicationContext context: Context,
        workerFactory: WorkerFactory
    ): WorkManager {
        val configuration = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        return WorkManager.initialize(context, configuration)
    }

//    @Provides
//    fun provideWorkerFactory(syncWorkerFactory: SyncWorkerFactory): WorkerFactory {
//        return syncWorkerFactory
//    }

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}