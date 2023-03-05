package com.aldidwikip.mygithubuser.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aldidwikip.mygithubuser.domain.model.UserDetail

@Entity(tableName = "user_favorite_table")
data class UserDetailEntity(
        @PrimaryKey val username: String,
        val avatar: String?,
        val name: String?,
        val location: String?,
        val company: String?,
        val followers: Int,
        val following: Int,
        val repositoryNum: Int,
        val id: Int
) {
    fun toDomain(): UserDetail = UserDetail(
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
