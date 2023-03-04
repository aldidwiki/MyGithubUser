package com.aldidwikip.mygithubuser.data

import com.aldidwikip.mygithubuser.data.source.remote.RemoteDataSource
import com.aldidwikip.mygithubuser.domain.model.User
import com.aldidwikip.mygithubuser.domain.model.UserDetail
import com.aldidwikip.mygithubuser.domain.repository.AppRepository
import com.aldidwikip.mygithubuser.helper.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
        private val remoteDataSource: RemoteDataSource
) : AppRepository {
    override fun getUsers(): Flow<DataState<List<User>>> {
        return remoteDataSource.getUsers()
    }

    override fun getUser(username: String): Flow<DataState<UserDetail>> {
        return remoteDataSource.getUser(username)
    }

    override fun getUserFollowing(username: String): Flow<DataState<List<User>>> {
        return remoteDataSource.getUserFollowing(username)
    }

    override fun getUserFollower(username: String): Flow<DataState<List<User>>> {
        return remoteDataSource.getUserFollower(username)
    }

    override fun getUserSearch(keyword: String): Flow<DataState<List<User>>> {
        return remoteDataSource.getUserSearch(keyword)
    }
}