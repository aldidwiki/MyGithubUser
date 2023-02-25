package com.aldidwikip.mygithubuser.ui.main

import androidx.lifecycle.*
import com.aldidwikip.mygithubuser.data.AppRepository
import com.aldidwikip.mygithubuser.data.model.Users
import com.aldidwikip.mygithubuser.helper.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {
    private val _searchUser: MutableLiveData<DataState<List<Users>>> = MutableLiveData()
    val searchUser: LiveData<DataState<List<Users>>> = _searchUser

    val users = appRepository.getUsers.asLiveData(viewModelScope.coroutineContext)

    fun getSearchUser(keyword: String) {
        viewModelScope.launch {
            appRepository.getSearchUser(keyword).collect { _searchUser.postValue(it) }
        }
    }
}