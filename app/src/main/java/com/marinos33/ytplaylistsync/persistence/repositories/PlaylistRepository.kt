package com.marinos33.ytplaylistsync.persistence.repositories

import com.marinos33.ytplaylistsync.persistence.entities.PlaylistEntity

interface PlaylistRepository {
     suspend fun getAll(): List<PlaylistEntity>

     suspend fun insert(playlist: PlaylistEntity): Long

     suspend fun delete(playlist: PlaylistEntity): Int

     suspend fun loadById(playlistId: Int): PlaylistEntity

     suspend fun updatePlaylistLastUpdate(playlistId: Int, lastUpdated: String)
}