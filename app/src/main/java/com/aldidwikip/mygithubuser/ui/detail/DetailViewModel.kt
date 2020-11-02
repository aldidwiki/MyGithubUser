package com.aldidwikip.mygithubuser.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldidwikip.mygithubuser.data.AppRepository
import com.aldidwikip.mygithubuser.data.User
import com.aldidwikip.mygithubuser.helper.DataState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailViewModel @ViewModelInject constructor(private val appRepository: AppRepository) : ViewModel() {
    private val _user: MutableLiveData<DataState<User>> = MutableLiveData()
    val user: LiveData<DataState<User>> = _user

    fun getUser(username: String) {
        viewModelScope.launch {
            appRepository.getUser(username).collect { _user.postValue(it) }
        }
    }
}