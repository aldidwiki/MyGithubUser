package com.aldidwikip.mygithubuser.di

import android.content.Context
import android.content.SharedPreferences
import com.aldidwikip.mygithubuser.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
                context.resources.getString(R.string.my_preference_name),
                Context.MODE_PRIVATE
        )
    }
}