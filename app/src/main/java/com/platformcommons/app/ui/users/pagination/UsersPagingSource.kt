package com.platformcommons.app.ui.users.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.platformcommons.app.api.UsersApiImpl
import com.platformcommons.app.model.Data
import com.platformcommons.app.model.UsersResponse
import javax.inject.Inject

class UsersPagingSource @Inject constructor(
    private val apiImpl: UsersApiImpl
): PagingSource<Int, Data>() {

    companion object {
        var USER_LIST_TOTAL_PAGES = 2
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        return try {
            val position = params.key ?: 1
            val response = apiImpl.getUsers(position)

            if (response.isSuccessful) {
                response.body()?.let { userData ->
                    return LoadResult.Page(
                        data = userData.data,
                        prevKey = if (position == 1) null else (position - 1),
                        nextKey = if (position == userData.total_pages) {
                            USER_LIST_TOTAL_PAGES = userData.total_pages
                            null
                        } else (position + 1)
                    )
                }

                LoadResult.Error(throw Exception("No Response"))
            } else {
                LoadResult.Error(throw Exception("No Response"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}