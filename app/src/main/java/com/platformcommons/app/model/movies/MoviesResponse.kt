package com.platformcommons.app.model.movies

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    val page: Int,
    @SerializedName("results")
    val moviesResults: List<MoviesResult>,
    val total_pages: Int,
    val total_results: Int
)