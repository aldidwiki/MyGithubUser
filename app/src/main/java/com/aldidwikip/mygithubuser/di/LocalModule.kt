package com.aldidwikip.mygithubuser.di

import android.content.Context
import androidx.room.Room
import com.aldidwikip.mygithubuser.data.local.LocalDatabase
import com.aldidwikip.mygithubuser.data.local.LocalDatabase.Companion.DB_NAME
import com.aldidwikip.mygithubuser.data.local.LocalService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(context, LocalDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideLocalService(database: LocalDatabase): LocalService = database.localService()
}