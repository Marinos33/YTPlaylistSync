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

        suspend fun fetchPlaylists(onFinishedListener: OnFinishedListener): List<PlaylistEntity>?
    }

    interface Presenter {

        // method to destroy
        // lifecycle of MainActivity
        fun onDestroy()

        fun fetchPlaylists(): List<PlaylistEntity>
    }
}