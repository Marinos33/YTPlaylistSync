package com.example.ytplaylistsync.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ytplaylistsync.persistence.dao.PlaylistDao
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity

@Database(entities = [PlaylistEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    }

