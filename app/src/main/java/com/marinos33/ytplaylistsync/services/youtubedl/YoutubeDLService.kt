package com.marinos33.ytplaylistsync.services.youtubedl

import android.content.Context
import com.marinos33.ytplaylistsync.persistence.entities.PlaylistEntity
import com.yausername.youtubedl_android.DownloadProgressCallback
import com.yausername.youtubedl_android.mapper.VideoInfo

interface YoutubeDLService {

    fun downloadPlaylist(playlist: PlaylistEntity, callback: DownloadProgressCallback, onSuccess: () -> Unit, onFailure: () -> Unit)

    fun downloadCustom(url: String, commands: String?, callback: DownloadProgressCallback, onSuccess: () -> Unit, onFailure: () -> Unit)

    suspend fun getInfo(url: String): VideoInfo?

    fun stopCustomDownload(): Boolean

    fun init(context: Context)

    fun updateYoutubeDL(context: Context, onSuccess: () -> Unit, onFailure: () -> Unit)
}