package com.aldidwikip.mygithubuser.data

import com.google.gson.annotations.SerializedName

data class UsersResponse(
        val items: List<Users>
)

data class Users(
        @SerializedName("login")
        val username: String,
        @SerializedName("avatar_url")
        val avatar: String,
        val id: Int
)

data class User(
        @SerializedName("login")
        val username: String,
        @SerializedName("avatar_url")
        val avatar: String?,
        val name: String?,
        val location: String?,
        val company: String?,
        val followers: Int,
        val following: Int,
        @SerializedName("public_repos")
        val repositoryNum: Int
)