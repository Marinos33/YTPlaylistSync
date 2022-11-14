package com.example.ytplaylistsync.persistence.repositories

import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
     suspend fun getAll(): List<PlaylistEntity>

     suspend fun insert(playlist: PlaylistEntity): Long

     suspend fun delete(playlist: PlaylistEntity): Int

     suspend fun loadById(playlistId: Int): PlaylistEntity

     suspend fun updatePlaylistLastUpdate(playlistId: Int, lastUpdated: String)
}