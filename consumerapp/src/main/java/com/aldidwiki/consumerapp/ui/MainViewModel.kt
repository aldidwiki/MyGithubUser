package com.aldidwiki.consumerapp.ui

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aldidwiki.consumerapp.data.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.aldidwiki.consumerapp.data.entity.UserProperty
import com.aldidwiki.consumerapp.helper.mapCursorToList
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val context: Application) : AndroidViewModel(context) {
    private val _favUser: MutableLiveData<List<UserProperty>> = MutableLiveData()
    val favUser: LiveData<List<UserProperty>> = _favUser

    fun loadFavUser() {
        viewModelScope.launch(Main) {
            val deferredUser = async(IO) {
                val cursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)
                mapCursorToList(cursor)
            }
            _favUser.postValue(deferredUser.await())
        }
    }
}