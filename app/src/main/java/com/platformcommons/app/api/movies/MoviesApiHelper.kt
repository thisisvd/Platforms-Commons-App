package com.platformcommons.app.api.movies

import com.platformcommons.app.model.UsersResponse
import com.platformcommons.app.model.movies.MoviesDetailsResponse
import com.platformcommons.app.model.movies.MoviesResponse
import retrofit2.Response

interface MoviesApiHelper {

    suspend fun getTrendingMovies(pageNumber: Int, apiKey: String): Response<MoviesResponse>

    suspend fun getMovieDetails(movieId: Int, apiKey: String): Response<MoviesDetailsResponse>
}