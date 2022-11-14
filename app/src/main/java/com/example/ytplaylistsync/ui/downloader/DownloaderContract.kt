package com.example.ytplaylistsync.ui.downloader

interface DownloaderContract {
    interface View {
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
    }
}