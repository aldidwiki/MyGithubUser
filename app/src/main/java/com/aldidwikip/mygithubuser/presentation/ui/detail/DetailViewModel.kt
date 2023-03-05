package com.aldidwikip.mygithubuser.presentation.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldidwikip.mygithubuser.data.AppRepositoryImpl
import com.aldidwikip.mygithubuser.domain.model.UserDetail
import com.aldidwikip.mygithubuser.helper.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailViewModel @Inject constructor(private val appRepository: AppRepositoryImpl) : ViewModel() {
    private var username = MutableStateFlow("")

    fun setUsername(username: String) {
        this.username.value = username
    }

    val getUser = username.flatMapLatest {
        appRepository.getUser(it)
    }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DataState.Loading
    )

    val getFavorite = username.flatMapLatest {
        appRepository.getFavorite(it)
    }.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            1
    )

    fun saveToFavorite(userDetail: UserDetail) {
        viewModelScope.launch { appRepository.saveFavorite(userDetail) }
    }

    fun deleteFavorite(userDetail: UserDetail) {
        viewModelScope.launch { appRepository.deleteFavorite(userDetail) }
    }
}