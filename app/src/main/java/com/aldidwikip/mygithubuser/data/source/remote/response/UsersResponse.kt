package com.aldidwikip.mygithubuser.data.source.remote.response

import com.aldidwikip.mygithubuser.domain.model.User
import com.google.gson.annotations.SerializedName

data class UsersResponseList(
        val items: List<UsersResponse>
)

data class UsersResponse(
        @SerializedName("login")
        val username: String,

        @SerializedName("avatar_url")
        val avatar: String,

        @SerializedName("id")
        val id: Int
) {
    fun toDomainModel() = User(
            username = username,
            id = id,
            avatar = avatar
    )
}
