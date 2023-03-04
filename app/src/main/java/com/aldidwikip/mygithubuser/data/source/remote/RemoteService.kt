package com.aldidwikip.mygithubuser.data.source.remote

import com.aldidwikip.mygithubuser.data.source.remote.response.UserResponse
import com.aldidwikip.mygithubuser.data.source.remote.response.UsersResponse
import com.aldidwikip.mygithubuser.data.source.remote.response.UsersResponseList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteService {

    @GET("users?per_page=15")
    suspend fun getUsers(): Response<List<UsersResponse>>

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<UserResponse>

    @GET("search/users")
    suspend fun getSearchUser(@Query("q") keyword: String): Response<UsersResponseList>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): Response<List<UsersResponse>>

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): Response<List<UsersResponse>>
}