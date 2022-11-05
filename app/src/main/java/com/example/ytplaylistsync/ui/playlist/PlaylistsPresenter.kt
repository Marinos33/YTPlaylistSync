package com.example.ytplaylistsync.ui.playlist

import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import com.example.ytplaylistsync.ui.playlist.modelResponse.OnAddPlaylist
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

    override fun addPlaylist(url: String): OnAddPlaylist {
        val result = runBlocking {
            val random = (1..100).random()
            model.addPlaylist("test$random", "test$random", "test$random", url,"test$random" )
        }
        return OnAddPlaylist(result.message, result.isSuccess)
    }


    override fun onFinished(string: String?) {
    }

}