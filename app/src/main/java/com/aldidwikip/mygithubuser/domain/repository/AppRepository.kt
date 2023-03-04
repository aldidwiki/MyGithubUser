package com.aldidwikip.mygithubuser.domain.repository

import com.aldidwikip.mygithubuser.domain.model.User
import com.aldidwikip.mygithubuser.domain.model.UserDetail
import com.aldidwikip.mygithubuser.helper.DataState
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getUsers(): Flow<DataState<List<User>>>

    fun getUser(username: String): Flow<DataState<UserDetail>>

    fun getUserFollowing(username: String): Flow<DataState<List<User>>>

    fun getUserFollower(username: String): Flow<DataState<List<User>>>

    fun getUserSearch(keyword: String): Flow<DataState<List<User>>>
}