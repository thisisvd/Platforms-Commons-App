package com.platformcommons.app.ui.movies.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.platformcommons.app.network.movies.MoviesApiImpl
import com.platformcommons.app.model.movies.MoviesResult
import com.platformcommons.app.ui.movies.pagination.MoviesPagingSource
import com.platformcommons.app.ui.movies.pagination.MoviesPagingSource.Companion.MOVIES_LIST_TOTAL_PAGES
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val apiImpl: MoviesApiImpl
) {

    fun getMoviesList(): Flow<PagingData<MoviesResult>> {
        return Pager(config = PagingConfig(
            pageSize = MOVIES_LIST_TOTAL_PAGES, enablePlaceholders = false
        ), pagingSourceFactory = { MoviesPagingSource(apiImpl) }).flow
    }

    suspend fun getMoviesDetails(movieId: Int, apiKey: String) =
        apiImpl.getMovieDetails(movieId, apiKey)
}