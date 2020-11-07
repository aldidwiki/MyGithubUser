package com.aldidwikip.mygithubuser.data

import android.util.Log
import com.aldidwikip.mygithubuser.data.local.LocalService
import com.aldidwikip.mygithubuser.data.model.UserProperty
import com.aldidwikip.mygithubuser.data.remote.RemoteService
import com.aldidwikip.mygithubuser.helper.DataState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AppRepository @Inject constructor(private val remoteService: RemoteService,
        private val localService: LocalService) {

    companion object {
        private const val TAG = "AppRepository"
    }

    val getUsers = flow {
        emit(DataState.Loading)
        try {
            val response = remoteService.getUsers()
            if (response.isSuccessful) {
                response.body()?.let { localService.saveUsers(it) }
            } else Log.e(TAG, "getUsers: ${response.errorBody()}")
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Log.e(TAG, "getUsers: ${e.message}")
        } finally {
            emit(DataState.Success(localService.getUsers()))
        }
    }.flowOn(IO)

    fun getUser(username: String) = flow {
        emit(DataState.Loading)
        try {
            val response = remoteService.getUser(username)
            if (response.isSuccessful) {
                response.body()?.let { localService.saveUser(it) }
            } else Log.e(TAG, "getUser: ${response.errorBody()}")
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Log.e(TAG, "getUser: ${e.message}")
        } finally {
            emit(DataState.Success(localService.getUser(username)))
        }
    }.flowOn(IO)

    fun getSearchUser(keyword: String) = flow {
        emit(DataState.Loading)
        try {
            val response = remoteService.getSearchUser(keyword)
            if (response.isSuccessful) {
                response.body()?.items?.let { emit(DataState.Success(it)) }
            } else Log.e(TAG, "getSearchUser: ${response.errorBody()}")
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Log.e(TAG, "getSearchUser: ${e.message}")
        }
    }.flowOn(IO)

    fun getFollowing(username: String) = flow {
        emit(DataState.Loading)
        try {
            val response = remoteService.getFollowing(username)
            if (response.isSuccessful) {
                response.body()?.let { emit(DataState.Success(it)) }
            } else Log.e(TAG, "getFollowing: ${response.errorBody()}")
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Log.e(TAG, "getFollowing: ${e.message}")
        }
    }.flowOn(IO)

    fun getFollowers(username: String) = flow {
        emit(DataState.Loading)
        try {
            val response = remoteService.getFollowers(username)
            if (response.isSuccessful) {
                response.body()?.let { emit(DataState.Success(it)) }
            } else Log.e(TAG, "getFollowers: ${response.errorBody()}")
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Log.e(TAG, "getFollowers: ${e.message}")
        }
    }.flowOn(IO)

    suspend fun saveFavorite(user: UserProperty) {
        try {
            localService.saveFavorite(user)
        } catch (e: Exception) {
            Log.e(TAG, "saveFavorite: ${e.message}")
        }
    }

    fun isFavorite(username: String) = flow {
        try {
            emit(localService.isFavorite(username))
        } catch (e: Exception) {
            Log.e(TAG, "isFavorite: ${e.message}")
        }
    }.flowOn(IO)

    suspend fun deleteFavorite(username: String) {
        try {
            localService.deleteFavorite(username)
        } catch (e: Exception) {
            Log.e(TAG, "deleteFavorite: ${e.message}")
        }
    }

    val getFavorite = localService.getFavorite()
}