package com.marinos33.ytplaylistsync.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marinos33.ytplaylistsync.persistence.dao.PlaylistDao
import com.marinos33.ytplaylistsync.persistence.entities.PlaylistEntity

@Database(entities = [PlaylistEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    }

