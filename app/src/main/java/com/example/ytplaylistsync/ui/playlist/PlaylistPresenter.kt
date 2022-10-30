package com.example.ytplaylistsync.ui.playlist

class PlaylistPresenter(
    private var mainView: PlaylistContract.View?,
    private val model: PlaylistContract.Model) : PlaylistContract.Presenter,
    PlaylistContract.Model.OnFinishedListener {


    override fun onDestroy() {
        mainView = null
    }

    override fun onFinished(string: String?) {
    }

}