package com.aldidwikip.mygithubuser.data.source.remote.response

import com.aldidwikip.mygithubuser.domain.model.UserDetail
import com.google.gson.annotations.SerializedName

data class UserResponse(
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
        val repositoryNum: Int,

        val id: Int
) {
    fun toDomainModel() = UserDetail(
            username = username,
            avatar = avatar,
            name = name,
            location = location,
            company = company,
            followers = followers,
            following = following,
            repositoryNum = repositoryNum,
            id = id
    )
}
