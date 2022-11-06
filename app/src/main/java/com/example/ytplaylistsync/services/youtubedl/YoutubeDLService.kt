package com.example.ytplaylistsync.services.youtubedl

import com.yausername.youtubedl_android.mapper.VideoInfo

interface YoutubeDLService {

    fun downLoadPlaylist(url: String)

    fun getInfoPlaylist(url: String): VideoInfo
}