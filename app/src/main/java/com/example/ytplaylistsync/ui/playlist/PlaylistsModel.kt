package com.example.ytplaylistsync.ui.playlist

import com.example.ytplaylistsync.persistence.AppDatabase


class PlaylistsModel : PlaylistsContract.Model {
    override fun fetchPlaylists(onFinishedListener: PlaylistsContract.Model.OnFinishedListener) {
        AppDatabase.getInstance().playlistDao().getAll()
    }

}