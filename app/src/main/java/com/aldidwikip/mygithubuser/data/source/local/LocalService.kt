package com.aldidwikip.mygithubuser.data.source.local

import androidx.room.*
import com.aldidwikip.mygithubuser.data.source.local.entity.UserDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalService {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavorite(userDetailEntity: UserDetailEntity)

    @Delete
    suspend fun deleteFavorite(userDetailEntity: UserDetailEntity)

    @Query("SELECT * FROM user_favorite_table")
    fun getFavorites(): Flow<List<UserDetailEntity>>

    @Query("SELECT * FROM user_favorite_table WHERE username = :username")
    fun getFavorite(username: String): Flow<UserDetailEntity>
}