package com.aldidwikip.mygithubuser.presentation.ui.favorite

import androidx.lifecycle.ViewModel
import com.aldidwikip.mygithubuser.data.AppRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val appRepository: AppRepositoryImpl) : ViewModel() {
//    val favoriteList = appRepository.getFavorite.asLiveData(viewModelScope.coroutineContext)
//
//    fun deleteFavorite(username: String) = viewModelScope.launch(IO) {
//        appRepository.deleteFavorite(username)
//    }
}