package com.example.ytplaylistsync.ui.downloader

import android.os.Handler
import android.os.Looper
import com.example.ytplaylistsync.services.youtubedl.YoutubeDLService
import kotlinx.coroutines.*
import kotlin.coroutines.suspendCoroutine

class DownloaderPresenter(
    private var mainView: DownloaderContract.View?,
    private var youtubeDL: YoutubeDLService,
    private val model: DownloaderContract.Model) : DownloaderContract.Presenter,
    DownloaderContract.Model.OnFinishedListener {

    override fun onDestroy() {
        mainView = null
    }

    override fun fetchInfo(url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                val info = youtubeDL.getInfo(url)

                if (info != null) {

                    var thumbnailUrl: String? = null

                    thumbnailUrl = if (info.thumbnail != null) {
                        info.thumbnail
                    } else if (info.thumbnails != null) {
                        info.thumbnails[0].url
                    } else {
                        null
                    }

                    launch(Dispatchers.Main) {
                        mainView?.setVideoData(info.title, thumbnailUrl)
                    }
                }
            }
        }
    }

    override fun onFinished(string: String?) {
    }

}