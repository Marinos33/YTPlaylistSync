package com.example.ytplaylistsync.ui.playlist

import com.example.ytplaylistsync.persistence.entities.PlaylistEntity

class PlaylistsPresenter(
    private var mainView: PlaylistsContract.View?,
    private val model: PlaylistsContract.Model) : PlaylistsContract.Presenter,
    PlaylistsContract.Model.OnFinishedListener {


    override fun onDestroy() {
        mainView = null
    }

    override fun onRefresh() {
        TODO("Not yet implemented")
    }

    override fun fetchPlaylists(): List<PlaylistEntity> {
        TODO("Not yet implemented")
    }

    override fun onFinished(string: String?) {
    }

}