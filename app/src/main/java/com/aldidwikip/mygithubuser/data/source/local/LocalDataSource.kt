package com.aldidwikip.mygithubuser.data.source.local

import android.util.Log
import com.aldidwikip.mygithubuser.data.source.local.entity.UserDetailEntity
import com.aldidwikip.mygithubuser.domain.model.UserDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val localService: LocalService) {
    suspend fun saveFavorite(userDetailEntity: UserDetailEntity) {
        localService.saveFavorite(userDetailEntity)
    }

    suspend fun deleteFavorite(userDetailEntity: UserDetailEntity) {
        localService.deleteFavorite(userDetailEntity)
    }

    fun getFavorites(): Flow<List<UserDetail>> = localService.getFavorites().map { favoriteList ->
        favoriteList.map { it.toDomain() }
    }

    fun getFavorite(username: String): Flow<UserDetail> = localService.getFavorite(username)
            .map { favorite ->
                favorite.toDomain()
            }.catch {
                Log.e("LocalDataSource", "error get favorite for $username")
            }
}