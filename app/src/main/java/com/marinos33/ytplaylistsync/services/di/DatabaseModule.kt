package com.marinos33.ytplaylistsync.services.di

import android.content.Context
import androidx.room.Room
import com.marinos33.ytplaylistsync.persistence.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context, AppDatabase::class.java,
            "YTPlaylistSync.db"
        )
            .fallbackToDestructiveMigration()
            .build()
}