package com.marinos33.ytplaylistsync.ui.downloader

import android.webkit.URLUtil
import com.marinos33.ytplaylistsync.services.youtubedl.YoutubeDLService
import kotlinx.coroutines.*

class DownloaderPresenter(
    private var mainView: DownloaderContract.View?,
    private var youtubeDL: YoutubeDLService,
    private val model: DownloaderContract.Model) : DownloaderContract.Presenter,
    DownloaderContract.Model.OnFinishedListener {

    private var downloading: Boolean = false

    override fun onDestroy() {
        mainView = null
    }

    override fun fetchInfo(url: String) {
        //if url is empty or not a valid url
        if (url.isEmpty() || !URLUtil.isValidUrl(url)) {
            return
        }

        mainView?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                val info = youtubeDL.getInfo(url)

                if (info != null) {

                    val thumbnailUrl: String? = if (info.thumbnail != null) {
                        info.thumbnail
                    } else if (info.thumbnails != null) {
                        info.thumbnails[0].url
                    } else {
                        null
                    }

                    launch(Dispatchers.Main) {
                        mainView?.hideLoading()
                        mainView?.setVideoData(info.title, thumbnailUrl)
                    }
                }
            }
        }
    }

    override fun downloadVideo(url: String, commands: String) {
        if (url.isEmpty() || !URLUtil.isValidUrl(url)) {
            mainView?.showInfoToast("Url empty or not valid")
            return
        }

        if(downloading){
            mainView?.showInfoToast("Cannot start download. a download is already in progress")
            return
        }

        downloading = true
        mainView?.showProgress()
        youtubeDL.downloadCustom(url, commands, { progress, etaInSeconds, line ->
            mainView?.setProgress(progress.toInt())
        }, {
            mainView?.showSuccessToast("Download finished successfully")
            mainView?.hideProgress()
            downloading = false
        }, {
            mainView?.showErrorToast("Download failed or partially failed")
            mainView?.hideProgress()
            downloading = false
        })
    }

    override fun stopDownload() {
        if(downloading){
            youtubeDL.stopCustomDownload()
            mainView?.showInfoToast("Download stopped")
            mainView?.hideProgress()
            downloading = false
        } else {
            mainView?.showInfoToast("No download in progress")
        }
    }

    override fun onFinished(string: String?) {
    }

}