package com.aldidwikip.mygithubuser.data

import android.util.Log
import com.aldidwikip.mygithubuser.util.DataState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AppRepository @Inject constructor(private val remoteService: RemoteService) {

    companion object {
        private const val TAG = "AppRepository"
    }

    fun getUsers() = flow {
        emit(DataState.Loading)
        try {
            val response = remoteService.getUsers()
            if (response.isSuccessful) {
                emit(DataState.Success(response.body()!!))
            } else Log.e(TAG, "getUsers: ${response.errorBody()}")
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Log.e(TAG, "getUsers: ${e.message}")
        }
    }.flowOn(IO)

    fun getUser(username: String) = flow {
        emit(DataState.Loading)
        try {
            val response = remoteService.getUser(username)
            if (response.isSuccessful) {
                emit(DataState.Success(response.body()!!))
            } else Log.e(TAG, "getUser: ${response.errorBody()}")
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Log.e(TAG, "getUser: ${e.message}")
        }
    }.flowOn(IO)

    fun getSearchUser(keyword: String) = flow {
        emit(DataState.Loading)
        try {
            val response = remoteService.getSearchUser(keyword)
            if (response.isSuccessful) {
                emit(DataState.Success(response.body()!!.items))
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
                emit(DataState.Success(response.body()!!))
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
                emit(DataState.Success(response.body()!!))
            } else Log.e(TAG, "getFollowers: ${response.errorBody()}")
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Log.e(TAG, "getFollowers: ${e.message}")
        }
    }.flowOn(IO)
}