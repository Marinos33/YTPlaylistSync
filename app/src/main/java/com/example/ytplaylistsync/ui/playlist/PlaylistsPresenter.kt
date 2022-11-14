package com.example.ytplaylistsync.ui.playlist

import android.util.Log
import com.example.ytplaylistsync.common.DbResponse
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import com.example.ytplaylistsync.services.youtubedl.YoutubeDLService
import com.example.ytplaylistsync.ui.playlist.modelResponse.OnAddPlaylist
import com.example.ytplaylistsync.ui.playlist.modelResponse.OnRemovePlaylist
import kotlinx.coroutines.*

class PlaylistsPresenter(
    private var mainView: PlaylistsContract.View?,
    private var youtubeDL: YoutubeDLService,
    private val model: PlaylistsContract.Model) : PlaylistsContract.Presenter,
    PlaylistsContract.Model.OnFinishedListener {


    override fun onDestroy() {
        mainView = null
    }

    override fun fetchPlaylists() : List<PlaylistEntity> {
        val result = runBlocking {
            model.fetchPlaylists(this@PlaylistsPresenter)
        }
        return result!!
    }

    override fun addPlaylist(url: String): OnAddPlaylist {
        val result = runBlocking {
            var info = youtubeDL.getInfoPlaylist(url)
            if(info != null){
                var thumbnailUrl: String? = null

                if(info.thumbnail != null) {
                    thumbnailUrl = info.thumbnail
                }else if(info.thumbnails != null) {
                    thumbnailUrl = info.thumbnails[0].url
                }

                var now: String = java.time.LocalDateTime.now().toString()

                model.addPlaylist(info.title, info.uploader, now, url, thumbnailUrl)
            }
            else{
                DbResponse("Error: Invalid playlist url", -1)
            }
        }
        return OnAddPlaylist(result.message, result.isSuccess)
    }

    override fun deletePlaylist(id: Int): OnRemovePlaylist {
        val result = runBlocking {
            model.deletePlaylist(id)
        }
        return OnRemovePlaylist(result.message, result.isSuccess)
    }

    override fun downloadPlaylist(
        id: Int,
        progressCallback: (value: Float) -> Unit,
        onFailure: () -> Unit,
        onSuccess: () -> Unit
    ) {
        val playlist = runBlocking {
            return@runBlocking model.loadById(id)
        }

        youtubeDL.downLoadPlaylist(playlist, { progress, etaInSeconds, line ->
            progressCallback(progress)
        }, {
            runBlocking {
                model.updatePlaylistLastUpdate(id, java.time.LocalDateTime.now().toString())
            }
            mainView?.refreshPlaylists()
            onSuccess()
        }, {
            runBlocking {
                model.updatePlaylistLastUpdate(id, java.time.LocalDateTime.now().toString())
            }
            mainView?.refreshPlaylists()
            onFailure()
        })
    }

    override fun downloadPlaylist(id: Int): Unit {
        val playlist = runBlocking {
            return@runBlocking model.loadById(id)
        }

        youtubeDL.downLoadPlaylist(playlist, { progress, etaInSeconds, line ->
                Log.d("YoutubeDL", "$progress% (ETA $etaInSeconds seconds)")
            }, {
                Log.d("YoutubeDL", "Download finished")
            }, {
                Log.d("YoutubeDL", "Download failed")
            })
    }

    override fun downloadPlaylist(id: Int, progressCallback: (value: Float) -> Unit) {
        val playlist = runBlocking {
            return@runBlocking model.loadById(id)
        }

        youtubeDL.downLoadPlaylist(playlist, { progress, etaInSeconds, line ->
            Log.d("YoutubeDL", "$progress% (ETA $etaInSeconds seconds)")
            progressCallback(progress)
        }, {
            Log.d("YoutubeDL", "Download finished")
        }, {
            Log.d("YoutubeDL", "Download failed")
        })
    }


    override fun onFinished(string: String?) {
    }

}