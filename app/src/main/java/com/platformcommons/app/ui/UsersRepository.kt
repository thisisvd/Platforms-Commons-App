package com.platformcommons.app.ui

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.platformcommons.app.model.AddUser
import com.platformcommons.app.api.UsersApiImpl
import com.platformcommons.app.model.Data
import com.platformcommons.app.ui.users.pagination.UsersPagingSource
import com.platformcommons.app.ui.users.pagination.UsersPagingSource.Companion.USER_LIST_TOTAL_PAGES
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val apiImpl: UsersApiImpl
) {

    fun getUsers(): Flow<PagingData<Data>> {
        return Pager(
            config = PagingConfig(
                pageSize = USER_LIST_TOTAL_PAGES,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UsersPagingSource(apiImpl) }
        ).flow
    }

    suspend fun addUser(request: AddUser) = apiImpl.addUser(request)
}