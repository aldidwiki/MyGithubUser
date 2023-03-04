package com.aldidwikip.mygithubuser.presentation.ui.detail

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

@HiltViewModel
class DetailViewModel @Inject constructor(private val appRepository: AppRepositoryImpl) : ViewModel() {
    private var username = MutableStateFlow("")

    fun setUsername(username: String) {
        this.username.value = username
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val getUser = username.flatMapLatest {
        appRepository.getUser(it)
    }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DataState.Loading
    )
}