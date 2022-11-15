package com.example.ytplaylistsync.ui.downloader

import android.util.Log
import com.example.ytplaylistsync.services.youtubedl.YoutubeDLService
import kotlinx.coroutines.runBlocking

class DownloaderPresenter(
    private var mainView: DownloaderContract.View?,
    private var youtubeDL: YoutubeDLService,
    private val model: DownloaderContract.Model) : DownloaderContract.Presenter,
    DownloaderContract.Model.OnFinishedListener {

    override fun onDestroy() {
        mainView = null
    }

    override fun fetchInfo(url: String) {
        val result = runBlocking {
            return@runBlocking youtubeDL.getInfoPlaylist(url)
        }

        if (result != null) {

            var thumbnailUrl: String? = null

            thumbnailUrl = if(result.thumbnail != null) {
                result.thumbnail
            } else if(result.thumbnails != null) {
                result.thumbnails[0].url
            } else {
                null
            }

            mainView?.setVideoData(result.title, thumbnailUrl)
        }

    }

    override fun onFinished(string: String?) {
    }

}