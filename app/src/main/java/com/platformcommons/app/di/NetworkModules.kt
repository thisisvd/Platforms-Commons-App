package com.platformcommons.app.di

import android.content.Context
import com.platformcommons.app.BuildConfig
import com.platformcommons.app.network.movies.MoviesApi
import com.platformcommons.app.network.movies.MoviesApiHelper
import com.platformcommons.app.network.movies.MoviesApiImpl
import com.platformcommons.app.network.users.UsersApi
import com.platformcommons.app.network.users.UsersApiHelper
import com.platformcommons.app.network.users.UsersApiImpl
import com.platformcommons.app.ui.movies.pagination.MoviesPagingSource
import com.platformcommons.app.ui.users.pagination.UsersPagingSource
import com.platformcommons.app.utils.Constants.MOVIES_API_URL
import com.platformcommons.app.utils.Constants.USER_API_URL
import com.platformcommons.app.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModules {

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    } else {
        OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    @Named("Users")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(USER_API_URL).client(okHttpClient).build()

    @Provides
    fun provideApiInterface(@Named("Users") retrofit: Retrofit): UsersApi =
        retrofit.create(UsersApi::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: UsersApiImpl): UsersApiHelper = apiHelper

    @Provides
    @Singleton
    fun provideUserPagingSource(apiHelper: UsersApiImpl): UsersPagingSource {
        return UsersPagingSource(apiHelper)
    }

    @Singleton
    @Provides
    @Named("Movies")
    fun provideMoviesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MOVIES_API_URL).client(okHttpClient).build()

    @Provides
    fun provideMoviesApiInterface(@Named("Movies") retrofit: Retrofit): MoviesApi =
        retrofit.create(MoviesApi::class.java)

    @Provides
    @Singleton
    fun provideMoviesApiApiHelper(apiHelper: MoviesApiImpl): MoviesApiHelper = apiHelper

    @Provides
    @Singleton
    fun provideMoviesPagingSource(apiHelper: MoviesApiImpl): MoviesPagingSource {
        return MoviesPagingSource(apiHelper)
    }

    @Provides
    @Singleton
    fun provideNetworkUtils(@ApplicationContext context: Context): NetworkUtils {
        return NetworkUtils(context)
    }
}