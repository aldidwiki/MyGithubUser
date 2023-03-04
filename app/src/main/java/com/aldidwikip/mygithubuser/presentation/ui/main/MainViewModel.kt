package com.aldidwikip.mygithubuser.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldidwikip.mygithubuser.data.AppRepositoryImpl
import com.aldidwikip.mygithubuser.helper.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appRepository: AppRepositoryImpl) : ViewModel() {
    private val userKeyword = MutableStateFlow("")
    fun setUserKeyword(keyword: String) {
        userKeyword.value = keyword
    }

    val getUsers = appRepository.getUsers().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DataState.Loading
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val getUserSearch = userKeyword.filter { it.isNotEmpty() }.flatMapLatest {
        appRepository.getUserSearch(it)
    }.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            1
    )
}