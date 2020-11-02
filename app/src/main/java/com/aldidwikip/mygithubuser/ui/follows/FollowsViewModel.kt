package com.aldidwikip.mygithubuser.ui.follows

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldidwikip.mygithubuser.data.AppRepository
import com.aldidwikip.mygithubuser.data.Users
import com.aldidwikip.mygithubuser.helper.DataState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FollowsViewModel @ViewModelInject constructor(private val appRepository: AppRepository) : ViewModel() {
    private val _userFollowing: MutableLiveData<DataState<List<Users>>> = MutableLiveData()
    private val _userFollowers: MutableLiveData<DataState<List<Users>>> = MutableLiveData()

    val userFollowing: LiveData<DataState<List<Users>>> = _userFollowing
    val userFollowers: LiveData<DataState<List<Users>>> = _userFollowers

    fun getFollows(username: String) {
        viewModelScope.launch {
            appRepository.getFollowing(username).collect { _userFollowing.postValue(it) }
            appRepository.getFollowers(username).collect { _userFollowers.postValue(it) }
        }
    }
}