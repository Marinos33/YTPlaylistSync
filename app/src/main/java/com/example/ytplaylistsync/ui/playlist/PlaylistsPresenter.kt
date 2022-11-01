package com.example.ytplaylistsync.ui.playlist

import android.util.Log
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import kotlinx.coroutines.*

class PlaylistsPresenter(
    private var mainView: PlaylistsContract.View?,
    private val model: PlaylistsContract.Model) : PlaylistsContract.Presenter,
    PlaylistsContract.Model.OnFinishedListener {


    override fun onDestroy() {
        mainView = null
    }

    override fun fetchPlaylists() : List<PlaylistEntity> {
        val result = runBlocking {
            model.fetchPlaylists(this@PlaylistsPresenter)
        }
        return result!!
    }

    override fun onFinished(string: String?) {
    }

}