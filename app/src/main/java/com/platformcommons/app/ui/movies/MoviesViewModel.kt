package com.platformcommons.app.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.platformcommons.app.model.movies.MoviesDetailsResponse
import com.platformcommons.app.model.movies.MoviesResult
import com.platformcommons.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    fun getMoviesList(): Flow<PagingData<MoviesResult>> =
        moviesRepository.getMoviesList().cachedIn(viewModelScope)

    private val _movieDetails =
        MutableStateFlow<Resource<MoviesDetailsResponse>>(Resource.Loading())
    val movieDetails: StateFlow<Resource<MoviesDetailsResponse>> = _movieDetails

    fun getMoviesDetails(movieId: Int) = viewModelScope.launch {
        _movieDetails.emit(Resource.Loading())
        val response = moviesRepository.getMoviesDetails(movieId, "b78dc7d57b8dded294cbc83a4f80c9ae")
        _movieDetails.emit(handleMoviesDetailsResponse(response))
    }

    private fun handleMoviesDetailsResponse(response: Response<MoviesDetailsResponse>): Resource<MoviesDetailsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}