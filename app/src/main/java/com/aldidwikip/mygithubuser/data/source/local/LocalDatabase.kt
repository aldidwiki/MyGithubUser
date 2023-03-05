package com.aldidwikip.mygithubuser.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aldidwikip.mygithubuser.data.source.local.entity.UserDetailEntity

@Database(entities = [UserDetailEntity::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun localService(): LocalService

    companion object {
        const val DB_NAME = "github_user_db"
    }
}