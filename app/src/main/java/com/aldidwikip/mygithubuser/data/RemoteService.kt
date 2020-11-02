package com.aldidwikip.mygithubuser.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteService {

    @GET("users?per_page=15")
    suspend fun getUsers(): Response<List<Users>>

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Response<User>

    @GET("search/users")
    suspend fun getSearchUser(@Query("q") keyword: String): Response<UsersResponse>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): Response<List<Users>>

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): Response<List<Users>>
}