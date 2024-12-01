package com.marinos33.ytplaylistsync.ui.playlist

import com.marinos33.ytplaylistsync.common.DbResponse
import com.marinos33.ytplaylistsync.persistence.entities.PlaylistEntity
import com.marinos33.ytplaylistsync.persistence.repositories.PlaylistRepository

class PlaylistsModel(
    private val repository: PlaylistRepository): PlaylistsContract.Model {

    override suspend fun fetchPlaylists(onFinishedListener: PlaylistsContract.Model.OnFinishedListener): List<PlaylistEntity> {
        return repository.getAll()
    }

    override suspend fun addPlaylist(
        name: String,
        author: String,
        lastUpdated: String,
        url: String,
        thumbnail: String?
    ): DbResponse {
        val playlist = PlaylistEntity(null, name, author, lastUpdated, url, thumbnail)
        return try{
            val rowAffected = repository.insert(playlist)
            return DbResponse("Playlist added successfully", rowAffected)
        } catch (e: Exception) {
            return DbResponse(e.message.toString(), -1)
        }
    }

    override suspend fun deletePlaylist(id: Int): DbResponse {
        return try{
            val playlistEntity = repository.loadById(id)
            val rowAffected = repository.delete(playlistEntity)
            return DbResponse("Playlist removed successfully", rowAffected.toLong())
        } catch (e: Exception) {
            return DbResponse(e.message.toString(), -1)
        }
    }

    override suspend fun loadById(id: Int): PlaylistEntity {
        return repository.loadById(id)
    }

    override suspend fun updatePlaylistLastUpdate(playlistId: Int, lastUpdated: String) {
        repository.updatePlaylistLastUpdate(playlistId, lastUpdated)
    }
}