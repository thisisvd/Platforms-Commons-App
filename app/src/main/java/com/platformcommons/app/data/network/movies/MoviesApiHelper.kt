package com.platformcommons.app.data.network.movies

import com.platformcommons.app.domain.movies.MoviesDetailsResponse
import com.platformcommons.app.domain.movies.MoviesResponse
import retrofit2.Response

interface MoviesApiHelper {

    suspend fun getTrendingMovies(pageNumber: Int, apiKey: String): Response<MoviesResponse>

    suspend fun getMovieDetails(movieId: Int, apiKey: String): Response<MoviesDetailsResponse>
}