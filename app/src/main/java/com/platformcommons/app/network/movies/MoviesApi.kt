package com.platformcommons.app.network.movies

import com.platformcommons.app.model.movies.MoviesDetailsResponse
import com.platformcommons.app.model.movies.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") pageNumber: Int = 1,
        @Query("api_key") apiKey: String
    ): Response<MoviesResponse>

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<MoviesDetailsResponse>
}