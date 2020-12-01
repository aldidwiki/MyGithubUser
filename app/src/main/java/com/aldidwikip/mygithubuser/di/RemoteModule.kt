package com.aldidwikip.mygithubuser.di

import com.aldidwikip.mygithubuser.BuildConfig
import com.aldidwikip.mygithubuser.data.remote.RemoteService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    private const val API_TOKEN = BuildConfig.API_TOKEN

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit {
        val httpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                            .addHeader("Authorization", API_TOKEN)
                            .build()
                    return@addInterceptor chain.proceed(request)
                }.build()

        return Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    @Singleton
    @Provides
    fun provideRemoteService(retrofit: Retrofit): RemoteService {
        return retrofit.create(RemoteService::class.java)
    }
}