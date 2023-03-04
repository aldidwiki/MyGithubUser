package com.aldidwikip.mygithubuser.presentation.ui.follows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldidwikip.mygithubuser.data.AppRepositoryImpl
import com.aldidwikip.mygithubuser.helper.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FollowsViewModel @Inject constructor(private val appRepository: AppRepositoryImpl) : ViewModel() {
    private val username = MutableStateFlow("")
    fun setUsername(username: String) {
        this.username.value = username
    }

    val userFollowing = username.flatMapLatest {
        appRepository.getUserFollowing(it)
    }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DataState.Loading
    )

    val userFollower = username.flatMapLatest {
        appRepository.getUserFollower(it)
    }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DataState.Loading
    )
}