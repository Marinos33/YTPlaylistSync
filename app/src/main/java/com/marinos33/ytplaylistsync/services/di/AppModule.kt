package com.marinos33.ytplaylistsync.services.di

import com.marinos33.ytplaylistsync.persistence.AppDatabase
import com.marinos33.ytplaylistsync.persistence.repositories.PlaylistRepoImpl
import com.marinos33.ytplaylistsync.persistence.repositories.PlaylistRepository
import com.marinos33.ytplaylistsync.services.youtubedl.YoutubeDLService
import com.marinos33.ytplaylistsync.services.youtubedl.YoutubeDLServiceImpl
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

    @Provides
    @Singleton
    fun provideYoutubeDlService(): YoutubeDLService = YoutubeDLServiceImpl()
}