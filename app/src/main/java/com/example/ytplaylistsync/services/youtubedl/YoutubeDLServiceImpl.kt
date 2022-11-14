package com.example.ytplaylistsync.services.youtubedl

import android.os.Environment
import android.util.Log
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import com.fasterxml.jackson.databind.ObjectMapper
import com.yausername.youtubedl_android.DownloadProgressCallback
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import com.yausername.youtubedl_android.mapper.VideoInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.operators.single.SingleDoOnSuccess
import io.reactivex.schedulers.Schedulers
import java.io.File

class YoutubeDLServiceImpl: YoutubeDLService {
    private val objectMapper = ObjectMapper()
    private val compositeDisposable = CompositeDisposable()
    //create random processId
    private val processId = (0..100000).random()

    override fun downLoadPlaylist(playlist: PlaylistEntity, callback: DownloadProgressCallback, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val youtubeDLDir = File(
            //TODO change download directory to music directory
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            playlist.name
        )

        //format playlist name to add \\ before every space
        val playlistName = playlist.name.replace(" ", "\\ ")

        //format playlist author to add \\ before every space
        //val playlistAuthor = playlist.author.replace(" ", "\\ ")

        var metadata = "-metadata album=${playlistName}"

        val request = YoutubeDLRequest(playlist.url)

        request.addOption("--extract-audio")
        request.addOption("--audio-format", "mp3")
        request.addOption("--audio-quality", "0")
        request.addOption("--add-metadata")
        //todo add thumbnail if set in settings
        //request.addOption("--write-thumbnail")
        //request.addOption("--embed-thumbnail")
        request.addOption("-f", "ba")
        request.addOption("--ignore-errors")
        request.addOption("--postprocessor-args", metadata)
        request.addOption("--yes-playlist")
        request.addOption("--download-archive", youtubeDLDir.absolutePath + "/archive.txt")
        request.addOption("-o", youtubeDLDir.absolutePath + "/%(title)s - %(uploader)s.%(ext)s")

        val disposable: Disposable = Observable.fromCallable {
            YoutubeDL.getInstance().execute(request, processId.toString(), callback)
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ youtubeDLResponse ->
                Log.d("YoutubeDL", youtubeDLResponse.out)
                onSuccess()
            }) { e ->
                Log.d("YoutubeDL", e.message.toString())
                onFailure()
            }

        compositeDisposable.add(disposable)
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
