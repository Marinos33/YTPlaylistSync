package com.example.ytplaylistsync.ui.playlist

import com.example.ytplaylistsync.persistence.entities.PlaylistEntity

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

        fun fetchPlaylists(onFinishedListener: OnFinishedListener)
    }

    interface Presenter {

        // method to destroy
        // lifecycle of MainActivity
        fun onDestroy()

        fun onRefresh()

        fun fetchPlaylists(): List<PlaylistEntity>
    }
}