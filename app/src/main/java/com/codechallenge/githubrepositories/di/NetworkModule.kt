package com.codechallenge.githubrepositories.di

import com.codechallenge.githubrepositories.network.Repository
import com.codechallenge.githubrepositories.network.RepositoryImpl
import com.codechallenge.githubrepositories.network.RepositoryService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun getUrl(): String = "https://api.github.com/"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, url: String): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideRepositoryService(retrofit: Retrofit): RepositoryService =
        retrofit.create(RepositoryService::class.java)

    @Provides
    @Singleton
    fun provideRepository(repository: RepositoryImpl): Repository = repository
}