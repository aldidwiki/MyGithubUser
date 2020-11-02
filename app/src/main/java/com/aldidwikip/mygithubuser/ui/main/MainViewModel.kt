package com.aldidwikip.mygithubuser.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.aldidwikip.mygithubuser.data.AppRepository
import com.aldidwikip.mygithubuser.data.Users
import com.aldidwikip.mygithubuser.util.DataState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val appRepository: AppRepository) : ViewModel() {
    private val _searchUser: MutableLiveData<DataState<List<Users>>> = MutableLiveData()
    val searchUser: LiveData<DataState<List<Users>>> = _searchUser

    val users = appRepository.getUsers()
            .asLiveData(viewModelScope.coroutineContext)

    fun getSearchUser(keyword: String) {
        viewModelScope.launch {
            appRepository.getSearchUser(keyword).collect { _searchUser.postValue(it) }
        }
    }
}