package com.example.ytplaylistsync.services.youtubedl

import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import com.yausername.youtubedl_android.DownloadProgressCallback
import com.yausername.youtubedl_android.mapper.VideoInfo

interface YoutubeDLService {

    fun downLoadPlaylist(playlist: PlaylistEntity, callback: DownloadProgressCallback, onSuccess: () -> Unit, onFailure: () -> Unit)

    suspend fun getInfoPlaylist(url: String): VideoInfo?
}