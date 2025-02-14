package com.platformcommons.app.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.platformcommons.app.api.UsersApiImpl
import com.platformcommons.app.model.Data
import com.platformcommons.app.model.UsersResponse
import com.platformcommons.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {

    fun getUsers(): Flow<PagingData<Data>> = usersRepository.getUsers()
    .cachedIn(viewModelScope)

//    fun addUser() = viewModelScope.launch {
//        _allUsers.emit(Resource.Loading())
//        val response = apiImpl.getUsers(1)
//        _allUsers.emit(handleUsersResponse(response))
//    }
//
//    private fun handleUsersResponse(response: Response<UsersResponse>): Resource<UsersResponse> {
//        if (response.isSuccessful) {
//            response.body()?.let { resultResponse ->
//                return Resource.Success(resultResponse)
//            }
//        }
//        return Resource.Error(response.message())
//    }
}