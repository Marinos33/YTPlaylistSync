package com.example.ytplaylistsync.services.di

import com.example.ytplaylistsync.persistence.AppDatabase
import com.example.ytplaylistsync.persistence.repositories.PlaylistRepoImpl
import com.example.ytplaylistsync.persistence.repositories.PlaylistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRepository(
        database: AppDatabase
    ): PlaylistRepository =
        PlaylistRepoImpl(database)
}