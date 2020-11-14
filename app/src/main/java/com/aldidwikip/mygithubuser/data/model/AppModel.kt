package com.aldidwikip.mygithubuser.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class UsersResponse(
        val items: List<Users>
)

@Entity(tableName = "users_table")
data class Users(
        @SerializedName("login")
        @PrimaryKey
        val username: String,
        @SerializedName("avatar_url")
        val avatar: String,
        val id: Int
)

@Entity(tableName = "user_detail_table")
data class User(
        @SerializedName("login")
        @PrimaryKey
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
)

@Entity(tableName = "favorite_table")
data class UserProperty(
        @PrimaryKey
        @ColumnInfo(name = "username_fav") val username: String,
        @ColumnInfo(name = "avatar_fav") val avatar: String,
        val isFavorite: Boolean
)

data class UserWithProperty(
        @Embedded val user: User,
        @Embedded val property: UserProperty?
)