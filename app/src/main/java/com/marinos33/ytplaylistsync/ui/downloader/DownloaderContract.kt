package com.marinos33.ytplaylistsync.ui.downloader

interface DownloaderContract {
    interface View {
        fun setVideoData(title: String, thumbnailUrl: String?)

        fun setProgress(progress: Int)

        fun showProgress()

        fun hideProgress()

        fun showSuccessToast(message: String)

        fun showErrorToast(message: String)

        fun showInfoToast(message: String)
    }

    interface Model {
        // nested interface to be
        interface OnFinishedListener {
            // function to be called
            // once the Handler of Model class
            // completes its execution
            fun onFinished(string: String?)
        }
    }

    interface Presenter {

        // method to destroy
        // lifecycle of MainActivity
        fun onDestroy()

        fun fetchInfo(url: String)

        fun downloadVideo(url: String, commands: String)

        fun stopDownload()
    }
}