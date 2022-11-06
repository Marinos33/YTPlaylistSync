package com.example.ytplaylistsync.services.youtubedl

import com.fasterxml.jackson.databind.ObjectMapper
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import com.yausername.youtubedl_android.mapper.VideoInfo

class YoutubeDLServiceImpl: YoutubeDLService {
    private val objectMapper = ObjectMapper()

    override fun downLoadPlaylist(url: String) {
        TODO("Not yet implemented")
    }

    override fun getInfoPlaylist(url: String): VideoInfo {
        var request =
            YoutubeDLRequest("https://www.youtube.com/playlist?list=PLa4gBFCX40Or71YuQszeR733MTU-s2Dkh")
        request.addOption("--flat-playlist")
        request.addOption("--dump-single-json")
        var response = YoutubeDL.getInstance().execute(request, null, null)

        return objectMapper.readValue(response.out, VideoInfo::class.java)
    }


}
