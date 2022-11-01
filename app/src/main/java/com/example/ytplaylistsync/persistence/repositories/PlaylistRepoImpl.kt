package com.example.ytplaylistsync.persistence.repositories

import com.example.ytplaylistsync.persistence.AppDatabase
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlaylistRepoImpl @Inject constructor(private val db: AppDatabase): PlaylistRepository {
    private val playlistDao = db.playlistDao()

    override suspend fun getAll(): List<PlaylistEntity> = playlistDao.getAll()
    override suspend fun insert(playlist: PlaylistEntity) = playlistDao.insert(playlist)
}