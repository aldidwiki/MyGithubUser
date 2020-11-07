package com.aldidwikip.mygithubuser.di

import com.aldidwikip.mygithubuser.data.AppRepository
import com.aldidwikip.mygithubuser.data.local.LocalService
import com.aldidwikip.mygithubuser.data.remote.RemoteService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppRepository(remoteService: RemoteService, localService: LocalService): AppRepository {
        return AppRepository(remoteService, localService)
    }
}