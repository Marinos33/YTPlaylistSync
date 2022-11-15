package com.example.ytplaylistsync.ui.playlist

import com.example.ytplaylistsync.common.DbResponse
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity

interface PlaylistsContract {
    interface View {
       fun refreshPlaylists(playlists: List<PlaylistEntity>)

       fun showSuccessToast(message: String)

       fun showErrorToast(message: String)

       fun showInfoToast(message: String)
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

        suspend fun loadById(id: Int): PlaylistEntity

        suspend fun updatePlaylistLastUpdate(playlistId: Int, lastUpdated: String)
    }

    interface Presenter {

        // method to destroy
        // lifecycle of MainActivity
        fun onDestroy()

        fun refreshPlaylists()

        fun addPlaylist(url: String)

        fun deletePlaylist(id: Int)

        fun downloadPlaylist(id: Int, progressCallback: (value: Float) -> Unit, onFailure: () -> Unit, onSuccess: () -> Unit)

        /*fun downloadPlaylist(id: Int): Unit

        fun downloadPlaylist(id: Int, progressCallback: (value: Float) -> Unit): Unit*/
    }
}