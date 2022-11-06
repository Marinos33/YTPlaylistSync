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

    override suspend fun getInfoPlaylist(url: String): VideoInfo? {
        return try{
            var request =
                YoutubeDLRequest(url)
            request.addOption("--flat-playlist")
            request.addOption("--dump-single-json")
            var response = YoutubeDL.getInstance().execute(request, null, null)

            objectMapper.readValue(response.out, VideoInfo::class.java)
        }catch (e: Exception){
            null
        }
    }


}
