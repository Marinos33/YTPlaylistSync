package com.example.ytplaylistsync.ui.playlist

import android.util.Log
import com.example.ytplaylistsync.common.DbResponse
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import com.example.ytplaylistsync.persistence.repositories.PlaylistRepository

class PlaylistsModel constructor(
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
            Log.e("PlaylistsModel", e.message.toString())
            return DbResponse(e.message.toString(), -1)
        }
    }

    override suspend fun deletePlaylist(id: Int): DbResponse {
        return try{
            val playlistEntity = repository.loadById(id)
            Log.d("PlaylistsModel", "playlist affected: $playlistEntity")
            val rowAffected = repository.delete(playlistEntity)
            Log.d("PlaylistsModel", "Row affected: $rowAffected")
            return DbResponse("Playlist removed successfully", rowAffected.toLong())
        } catch (e: Exception) {
            Log.e("PlaylistsModel", e.message.toString())
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