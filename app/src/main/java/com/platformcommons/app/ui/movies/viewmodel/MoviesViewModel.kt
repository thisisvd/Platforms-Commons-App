package com.platformcommons.app.ui.movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.platformcommons.app.domain.movies.MoviesDetailsResponse
import com.platformcommons.app.domain.movies.MoviesResult
import com.platformcommons.app.data.repository.MoviesRepository
import com.platformcommons.app.core.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _moviesListData = MutableStateFlow<PagingData<MoviesResult>?>(null)
    val moviesListData: StateFlow<PagingData<MoviesResult>?> = _moviesListData

    fun getMoviesList() = viewModelScope.launch {
        moviesRepository.getMoviesList().cachedIn(viewModelScope).collect { pagingData ->
            _moviesListData.emit(pagingData)
        }
    }

    private val _movieDetails =
        MutableStateFlow<Resource<MoviesDetailsResponse>>(Resource.Loading())
    val movieDetails: StateFlow<Resource<MoviesDetailsResponse>> = _movieDetails

    fun getMoviesDetails(movieId: Int) = viewModelScope.launch {
        _movieDetails.emit(Resource.Loading())
        val response =
            moviesRepository.getMoviesDetails(movieId, "b78dc7d57b8dded294cbc83a4f80c9ae")
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