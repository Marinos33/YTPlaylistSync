package com.example.ytplaylistsync.services.youtubedl

import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import com.yausername.youtubedl_android.DownloadProgressCallback
import com.yausername.youtubedl_android.mapper.VideoInfo

interface YoutubeDLService {

    fun downloadPlaylist(playlist: PlaylistEntity, callback: DownloadProgressCallback, onSuccess: () -> Unit, onFailure: () -> Unit)

    fun downloadCustom(url: String, commands: String?, callback: DownloadProgressCallback, onSuccess: () -> Unit, onFailure: () -> Unit)

    suspend fun getInfo(url: String): VideoInfo?

    fun stopCustomDownload(): Boolean
}