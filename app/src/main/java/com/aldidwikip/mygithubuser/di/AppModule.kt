package com.aldidwikip.mygithubuser.di

import android.content.Context
import android.content.SharedPreferences
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.data.AppRepository
import com.aldidwikip.mygithubuser.data.local.LocalService
import com.aldidwikip.mygithubuser.data.remote.RemoteService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppRepository(
            remoteService: RemoteService,
            localService: LocalService
    ): AppRepository {
        return AppRepository(remoteService, localService)
    }

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
                context.resources.getString(R.string.my_preference_name),
                Context.MODE_PRIVATE
        )
    }
}