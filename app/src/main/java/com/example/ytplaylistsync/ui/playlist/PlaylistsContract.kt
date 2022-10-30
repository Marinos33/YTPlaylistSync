package com.example.ytplaylistsync.ui.playlist

interface PlaylistsContract {
    interface View {
    }

    interface Model {
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