package com.platformcommons.app.data.network.movies

import com.platformcommons.app.domain.movies.MoviesDetailsResponse
import com.platformcommons.app.domain.movies.MoviesResponse
import retrofit2.Response
import javax.inject.Inject

class MoviesApiImpl @Inject constructor(
    private val api: MoviesApi
) : MoviesApiHelper {

    override suspend fun getTrendingMovies(
        pageNumber: Int, apiKey: String
    ): Response<MoviesResponse> = api.getTrendingMovies(pageNumber = pageNumber, apiKey = apiKey)

    override suspend fun getMovieDetails(
        movieId: Int, apiKey: String
    ): Response<MoviesDetailsResponse> = api.getMovieDetails(movieId, apiKey)
}