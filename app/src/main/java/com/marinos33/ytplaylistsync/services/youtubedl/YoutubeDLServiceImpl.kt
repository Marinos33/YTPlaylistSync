package com.marinos33.ytplaylistsync.services.youtubedl

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.util.Log
import com.marinos33.ytplaylistsync.BuildConfig
import com.marinos33.ytplaylistsync.persistence.entities.PlaylistEntity
import com.marinos33.ytplaylistsync.services.preferences.PrefsManager
import com.fasterxml.jackson.databind.ObjectMapper
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.DownloadProgressCallback
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import com.yausername.youtubedl_android.mapper.VideoInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File

class YoutubeDLServiceImpl: YoutubeDLService {
    private val objectMapper = ObjectMapper()
    private val compositeDisposable = CompositeDisposable()
    //create random processId
    private val processId = (0..100000).random()
    private var customProcessId: Int = 0

    override fun downloadPlaylist(playlist: PlaylistEntity, callback: DownloadProgressCallback, onSuccess: () -> Unit, onFailure: () -> Unit) {
        //format playlist name to add \\ before every space
        val playlistName = playlist.name.replace(" ", "\\ ")

        //remove every non ascii character in playlistname
        val playlistNameAscii = playlistName.replace("[^\\x00-\\x7F]".toRegex(), "")

        val youtubeDLDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
            playlist.name
        )

        //format playlist author to add \\ before every space
        //val playlistAuthor = playlist.author.replace(" ", "\\ ")

        val metadata = "-metadata album=${playlistNameAscii}"

        val request = YoutubeDLRequest(playlist.url)

        request.addOption("--extract-audio")
        request.addOption("--audio-format", "mp3")
        request.addOption("--audio-quality", "0")
        request.addOption("--add-metadata")
        request.addOption("--restrict-filenames")
        PrefsManager.getBoolean("use_thumbnail", false).let {
            if(it){
                request.addOption("--write-thumbnail")
                request.addOption("--embed-thumbnail")
            }
        }
        request.addOption("-f", "ba")
        request.addOption("--ignore-errors")
        request.addOption("--postprocessor-args", metadata)
        request.addOption("--yes-playlist")
        PrefsManager.getBoolean("use_archive", true).let {
            if(it){
                request.addOption("--download-archive", youtubeDLDir.absolutePath + "/archive.txt")
            }
        }
        request.addOption("-o", youtubeDLDir.absolutePath + "/%(title)s - %(uploader)s.%(ext)s")

        val disposable: Disposable = Observable.fromCallable {
            YoutubeDL.getInstance().execute(request, processId.toString(), callback)
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ youtubeDLResponse ->
                //Log.d("YoutubeDL", youtubeDLResponse.out)
                onSuccess()
            }) { e ->
                //Log.d("YoutubeDL", e.message.toString())
                onFailure()
            }

        compositeDisposable.add(disposable)
    }

    override fun downloadCustom(
        url: String,
        commands: String?,
        callback: DownloadProgressCallback,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val youtubeDLDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val request = YoutubeDLRequest(url)

        if(commands == null || commands.trim() == ""){
            request.addOption("--extract-audio")
            request.addOption("--audio-format", "mp3")
            request.addOption("--audio-quality", "0")
            request.addOption("--add-metadata")
            PrefsManager.getBoolean("use_thumbnail", false).let {
                if(it){
                    request.addOption("--write-thumbnail")
                    request.addOption("--embed-thumbnail")
                }
            }
            request.addOption("-f", "ba")
            request.addOption("--ignore-errors")
            request.addOption("--no-playlist")
            request.addOption("-o", youtubeDLDir.absolutePath + "/%(title)s - %(uploader)s.%(ext)s")
        }
        else{
            val commandsList = commands.split(" ")
            request.addCommands(commandsList)
            request.addOption("--restrict-filenames")
            request.addOption("-o", youtubeDLDir.absolutePath + "/%(title)s - %(uploader)s.%(ext)s")
        }

        customProcessId = (0..100000).random()

        val disposable: Disposable = Observable.fromCallable {
            YoutubeDL.getInstance().execute(request, customProcessId.toString(), callback)
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ youtubeDLResponse ->
                //Log.d("YoutubeDL", youtubeDLResponse.out)
                onSuccess()
            }) { e ->
                //Log.d("YoutubeDL", e.message.toString())
                onFailure()
            }

        compositeDisposable.add(disposable)
    }



    override suspend fun getInfo(url: String): VideoInfo? {
        return try{
            val request =
                YoutubeDLRequest(url)
            request.addOption("--flat-playlist")
            request.addOption("--dump-single-json")
            val response = YoutubeDL.getInstance().execute(request, null, null)

            objectMapper.readValue(response.out, VideoInfo::class.java)
        }catch (e: Exception){
            null
        }
    }

    override fun stopCustomDownload(): Boolean {
        return try {
            YoutubeDL.getInstance().destroyProcessById(customProcessId.toString())
            true
        } catch (e: Exception){
            false
        }
    }

    override fun init(context: Context) {
        YoutubeDL.getInstance().init(context)
        FFmpeg.getInstance().init(context)
    }

    override fun updateYoutubeDL(context: Context, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val disposable: Disposable = Observable.fromCallable {
            YoutubeDL.getInstance().updateYoutubeDL(context)
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ status ->
                onSuccess()
            }) { e ->
                if (BuildConfig.DEBUG) Log.e(
                    ContentValues.TAG,
                    "failed to update",
                    e
                )
                onFailure()
            }
        compositeDisposable.add(disposable)
    }
}
