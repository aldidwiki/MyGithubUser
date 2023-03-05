package com.aldidwikip.mygithubuser.di

import android.content.Context
import androidx.room.Room
import com.aldidwikip.mygithubuser.data.source.local.LocalDatabase
import com.aldidwikip.mygithubuser.data.source.local.LocalService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideLocalDatabase(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(
                context,
                LocalDatabase::class.java,
                LocalDatabase.DB_NAME
        )
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideLocalService(database: LocalDatabase): LocalService {
        return database.localService()
    }
}