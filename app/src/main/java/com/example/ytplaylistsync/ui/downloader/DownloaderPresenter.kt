package com.example.ytplaylistsync.ui.downloader

class DownloaderPresenter(
    private var mainView: DownloaderContract.View?,
    private val model: DownloaderContract.Model) : DownloaderContract.Presenter,
    DownloaderContract.Model.OnFinishedListener {

    override fun onDestroy() {
        mainView = null
    }

    override fun onFinished(string: String?) {
    }

}