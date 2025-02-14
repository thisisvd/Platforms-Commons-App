package com.platformcommons.app.ui.movies.pagination

import androidx.core.content.ContextCompat
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.platformcommons.app.api.movies.MoviesApiImpl
import com.platformcommons.app.model.movies.MoviesResult
import javax.inject.Inject

class MoviesPagingSource @Inject constructor(
    private val apiImpl: MoviesApiImpl
) : PagingSource<Int, MoviesResult>() {

    companion object {
        var MOVIES_LIST_TOTAL_PAGES = 2
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesResult> {
        return try {
            val position = params.key ?: 1
            val response = apiImpl.getTrendingMovies(position, "b78dc7d57b8dded294cbc83a4f80c9ae")

            if (response.isSuccessful) {
                response.body()?.let { userData ->
                    return LoadResult.Page(
                        data = userData.moviesResults,
                        prevKey = if (position == 1) null else (position - 1),
                        nextKey = if (position == userData.total_pages) {
                            MOVIES_LIST_TOTAL_PAGES = userData.total_pages
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

    override fun getRefreshKey(state: PagingState<Int, MoviesResult>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}