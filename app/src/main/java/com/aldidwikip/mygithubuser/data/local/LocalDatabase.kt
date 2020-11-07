package com.aldidwikip.mygithubuser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aldidwikip.mygithubuser.data.model.User
import com.aldidwikip.mygithubuser.data.model.UserProperty
import com.aldidwikip.mygithubuser.data.model.Users

@Database(entities = [Users::class, User::class, UserProperty::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun localService(): LocalService

    companion object {
        const val DB_NAME = "github_user_db"
    }
}