package com.example.ytplaylistsync.ui.playlist

import com.example.ytplaylistsync.common.DbResponse
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import com.example.ytplaylistsync.ui.playlist.modelResponse.OnAddPlaylist
import com.example.ytplaylistsync.ui.playlist.modelResponse.OnRemovePlaylist

interface PlaylistsContract {
    interface View {
       fun refreshPlaylists()

    }

    interface Model {
        interface OnFinishedListener {
            // function to be called
            // once the Handler of Model class
            // completes its execution
            fun onFinished(string: String?)
        }

        suspend fun fetchPlaylists(onFinishedListener: OnFinishedListener): List<PlaylistEntity>?

        suspend fun addPlaylist(name: String, author: String, lastUpdated: String, url: String, thumbnail: String?): DbResponse

        suspend fun deletePlaylist(id: Int): DbResponse
    }

    interface Presenter {

        // method to destroy
        // lifecycle of MainActivity
        fun onDestroy()

        fun fetchPlaylists(): List<PlaylistEntity>

        fun addPlaylist(url: String): OnAddPlaylist

        fun deletePlaylist(id: Int): OnRemovePlaylist
    }
}