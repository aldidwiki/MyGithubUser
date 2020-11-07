package com.aldidwikip.mygithubuser.ui.favorite

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aldidwikip.mygithubuser.data.AppRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FavoriteViewModel @ViewModelInject constructor(private val appRepository: AppRepository) : ViewModel() {
    val favoriteList = appRepository.getFavorite.asLiveData(viewModelScope.coroutineContext)

    fun deleteFavorite(username: String) = viewModelScope.launch(IO) {
        appRepository.deleteFavorite(username)
    }
}