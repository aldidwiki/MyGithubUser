package com.aldidwikip.mygithubuser.presentation.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldidwikip.mygithubuser.data.AppRepositoryImpl
import com.aldidwikip.mygithubuser.domain.model.UserDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val appRepository: AppRepositoryImpl) : ViewModel() {
    val userFavorites = appRepository.getFavorites().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
    )

    fun deleteFavorite(userDetail: UserDetail) {
        viewModelScope.launch { appRepository.deleteFavorite(userDetail) }
    }
}