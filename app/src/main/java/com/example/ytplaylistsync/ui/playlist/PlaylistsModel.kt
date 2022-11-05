package com.example.ytplaylistsync.ui.playlist

import android.util.Log
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import com.example.ytplaylistsync.persistence.repositories.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistsModel constructor(
    private val repository: PlaylistRepository): PlaylistsContract.Model {

    override suspend fun fetchPlaylists(onFinishedListener: PlaylistsContract.Model.OnFinishedListener): List<PlaylistEntity>? {
        return repository.getAll()
    }

    override suspend fun addPlaylist(
        name: String,
        author: String,
        lastUpdated: String,
        url: String,
        thumbnail: String
    ): Long {
        val playlist = PlaylistEntity(null, name, author, lastUpdated, url, thumbnail)
        return try{
            repository.insert(playlist)
        } catch (e: Exception) {
            Log.e("PlaylistsModel", e.message.toString())
            -1
        }
    }
}