package com.aldidwikip.mygithubuser.domain.model

data class UserDetail(
        val username: String,
        val avatar: String?,
        val name: String?,
        val location: String?,
        val company: String?,
        val followers: Int,
        val following: Int,
        val repositoryNum: Int,
        val id: Int
)
