package com.marinos33.ytplaylistsync.persistence.repositories

import com.marinos33.ytplaylistsync.persistence.AppDatabase
import com.marinos33.ytplaylistsync.persistence.entities.PlaylistEntity
import javax.inject.Inject

class PlaylistRepoImpl @Inject constructor(db: AppDatabase): PlaylistRepository {
    private val playlistDao = db.playlistDao()

    override suspend fun getAll(): List<PlaylistEntity> = playlistDao.getAll()
    override suspend fun insert(playlist: PlaylistEntity): Long = playlistDao.insert(playlist)
    override suspend fun delete(playlist: PlaylistEntity): Int = playlistDao.delete(playlist)
    override suspend fun loadById(playlistId: Int): PlaylistEntity = playlistDao.loadById(playlistId)
    override suspend fun updatePlaylistLastUpdate(playlistId: Int, lastUpdated: String)  = playlistDao.updatePlaylistLastUpdate(playlistId, lastUpdated)
}