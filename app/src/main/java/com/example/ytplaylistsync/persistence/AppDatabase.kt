package com.example.ytplaylistsync.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ytplaylistsync.persistence.dao.PlaylistDao
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity

@Database(entities = [PlaylistEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    }

