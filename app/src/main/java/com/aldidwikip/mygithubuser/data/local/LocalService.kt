package com.aldidwikip.mygithubuser.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aldidwikip.mygithubuser.data.model.User
import com.aldidwikip.mygithubuser.data.model.UserProperty
import com.aldidwikip.mygithubuser.data.model.UserWithProperty
import com.aldidwikip.mygithubuser.data.model.Users
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalService {

    @Query("SELECT * FROM users_table ORDER BY username ASC")
    fun getUsers(): List<Users>

    @Query("SELECT * FROM user_detail_table WHERE username = :username")
    fun getUser(username: String): User

    @Query("SELECT * FROM favorite_table WHERE username_fav = :username")
    fun isFavorite(username: String): UserProperty

    @Query("SELECT * FROM user_detail_table LEFT JOIN favorite_table ON username = username_fav WHERE isFavorite = 1 ORDER BY username ASC")
    fun getFavorite(): Flow<List<UserWithProperty>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUsers(users: List<Users>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavorite(user: UserProperty)

    @Query("DELETE FROM favorite_table WHERE username_fav = :username")
    suspend fun deleteFavorite(username: String)
}