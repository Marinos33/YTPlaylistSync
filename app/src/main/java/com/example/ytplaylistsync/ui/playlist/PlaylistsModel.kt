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
        //generate random number
        /*val random = (1..100).random()
        var playlisttoadd = PlaylistEntity(random,"test$random", "test$random", "test$random", "test$random","test$random" )
        repository.insert(playlisttoadd)*/
            return repository.getAll()
    }
}