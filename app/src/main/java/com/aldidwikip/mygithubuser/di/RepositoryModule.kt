package com.aldidwikip.mygithubuser.di

import com.aldidwikip.mygithubuser.data.AppRepositoryImpl
import com.aldidwikip.mygithubuser.domain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAppRepository(appRepositoryImpl: AppRepositoryImpl): AppRepository
}