package com.example.ytplaylistsync.ui.playlist

class PlaylistsPresenter(
    private var mainView: PlaylistsContract.View?,
    private val model: PlaylistsContract.Model) : PlaylistsContract.Presenter,
    PlaylistsContract.Model.OnFinishedListener {


    override fun onDestroy() {
        mainView = null
    }

    override fun onFinished(string: String?) {
    }

}