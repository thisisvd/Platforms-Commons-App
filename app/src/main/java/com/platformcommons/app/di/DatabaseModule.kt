package com.platformcommons.app.di

import android.content.Context
import androidx.room.Room
import com.platformcommons.app.data.local.UserDatabase
import com.platformcommons.app.utils.Constants.USER_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideUserDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app, UserDatabase::class.java, USER_DATABASE
    ).build()

    @Singleton
    @Provides
    fun provideUserDao(db: UserDatabase) = db.dao
}