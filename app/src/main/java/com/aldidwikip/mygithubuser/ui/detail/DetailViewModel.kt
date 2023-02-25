package com.aldidwikip.mygithubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldidwikip.mygithubuser.data.AppRepository
import com.aldidwikip.mygithubuser.data.model.User
import com.aldidwikip.mygithubuser.data.model.UserProperty
import com.aldidwikip.mygithubuser.helper.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {
    private val _user: MutableLiveData<DataState<User>> = MutableLiveData()
    val user: LiveData<DataState<User>> = _user

    fun getUser(username: String) {
        viewModelScope.launch(IO) {
            appRepository.getUser(username).collect { _user.postValue(it) }
        }
    }

    fun saveFavorite(user: UserProperty) = viewModelScope.launch(IO) {
        appRepository.saveFavorite(user)
    }

    suspend fun isFavorite(username: String): Boolean {
        var isFavorite = false
        appRepository.isFavorite(username).collect {
            isFavorite = it.isFavorite
        }
        return isFavorite
    }

    fun deleteFavorite(username: String) = viewModelScope.launch(IO) {
        appRepository.deleteFavorite(username)
    }
}