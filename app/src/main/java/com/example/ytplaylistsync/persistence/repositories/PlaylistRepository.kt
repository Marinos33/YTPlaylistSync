package com.example.ytplaylistsync.persistence.repositories

import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
     suspend fun getAll(): List<PlaylistEntity>

     suspend fun insert(playlist: PlaylistEntity): Long
}