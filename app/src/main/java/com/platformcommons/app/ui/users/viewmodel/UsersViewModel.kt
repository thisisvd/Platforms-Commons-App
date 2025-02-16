package com.platformcommons.app.ui.users.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.platformcommons.app.data.local.model.User
import com.platformcommons.app.domain.users.Data
import com.platformcommons.app.domain.users.NewUserResponse
import com.platformcommons.app.data.repository.UsersRepository
import com.platformcommons.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _userListData = MutableStateFlow<PagingData<Data>?>(null)
    val userList: StateFlow<PagingData<Data>?> = _userListData

    fun getUsersList() = viewModelScope.launch {
        usersRepository.getUsers().cachedIn(viewModelScope).collect { pagingData ->
            _userListData.emit(pagingData)
        }
    }

    private val _userDetails = MutableStateFlow<Resource<NewUserResponse>?>(null)
    val userDetails: StateFlow<Resource<NewUserResponse>?> = _userDetails

    fun addUser(user: User) = viewModelScope.launch {
        _userDetails.emit(Resource.Loading())
        val response = usersRepository.addUser(user)
        _userDetails.emit(handleUsersResponse(response))
    }

    private fun handleUsersResponse(response: Response<NewUserResponse>): Resource<NewUserResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}