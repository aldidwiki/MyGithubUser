package com.aldidwikip.mygithubuser.data.source.remote

import com.aldidwikip.mygithubuser.domain.model.User
import com.aldidwikip.mygithubuser.domain.model.UserDetail
import com.aldidwikip.mygithubuser.helper.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val remoteService: RemoteService) {
    fun getUsers(): Flow<DataState<List<User>>> = flow {
        emit(DataState.Loading)
        val response = remoteService.getUsers()
        if (response.isSuccessful) {
            response.body()?.let { usersResponses ->
                emit(DataState.Success(usersResponses.map { it.toDomainModel() }))
            }
        } else {
            emit(DataState.Error(response.message()))
        }
    }.catch { t ->
        emit(DataState.Error(t.localizedMessage))
    }.flowOn(Dispatchers.IO)

    fun getUser(username: String): Flow<DataState<UserDetail>> = flow {
        emit(DataState.Loading)
        val response = remoteService.getUser(username)
        if (response.isSuccessful) {
            response.body()?.let { userResponse ->
                emit(DataState.Success(userResponse.toDomainModel()))
            }
        } else {
            emit(DataState.Error(response.message()))
        }
    }.catch { t ->
        emit(DataState.Error(t.localizedMessage))
    }.flowOn(Dispatchers.IO)

    fun getUserFollowing(username: String): Flow<DataState<List<User>>> = flow {
        emit(DataState.Loading)
        val response = remoteService.getFollowing(username)
        if (response.isSuccessful) {
            response.body()?.let { usersResponses ->
                emit(DataState.Success(usersResponses.map { it.toDomainModel() }))
            }
        } else {
            emit(DataState.Error(response.message()))
        }
    }.catch { t ->
        emit(DataState.Error(t.localizedMessage))
    }.flowOn(Dispatchers.IO)

    fun getUserFollower(username: String): Flow<DataState<List<User>>> = flow {
        emit(DataState.Loading)
        val response = remoteService.getFollowers(username)
        if (response.isSuccessful) {
            response.body()?.let { usersResponses ->
                emit(DataState.Success(usersResponses.map { it.toDomainModel() }))
            }
        } else {
            emit(DataState.Error(response.message()))
        }
    }.catch { t ->
        emit(DataState.Error(t.localizedMessage))
    }.flowOn(Dispatchers.IO)

    fun getUserSearch(keyword: String): Flow<DataState<List<User>>> = flow {
        emit(DataState.Loading)
        val response = remoteService.getSearchUser(keyword)
        if (response.isSuccessful) {
            response.body()?.let { usersResponseList ->
                emit(DataState.Success(usersResponseList.items.map { it.toDomainModel() }))
            }
        } else {
            emit(DataState.Error(response.message()))
        }
    }.catch { t ->
        emit(DataState.Error(t.localizedMessage))
    }.flowOn(Dispatchers.IO)
}