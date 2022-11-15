package com.example.ytplaylistsync.ui.playlist

import android.util.Log
import android.widget.Toast
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

    override fun refreshPlaylists() {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
               var playlists = model.fetchPlaylists(this@PlaylistsPresenter)

                    launch(Dispatchers.Main) {
                        if (playlists != null) {
                            mainView?.refreshPlaylists(playlists)
                        }
                    }
                }
            }
    }

    override fun addPlaylist(url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                var info = youtubeDL.getInfo(url)
                if (info != null) {
                    var thumbnailUrl: String? = null

                    if (info.thumbnail != null) {
                        thumbnailUrl = info.thumbnail
                    } else if (info.thumbnails != null) {
                        thumbnailUrl = info.thumbnails[0].url
                    }

                    var now: String = java.time.LocalDateTime.now().toString()

                    var result: DbResponse =
                        model.addPlaylist(info.title, info.uploader, now, url, thumbnailUrl)

                    launch(Dispatchers.Main) {
                        if (result?.isSuccess == true) {
                            refreshPlaylists()
                            mainView?.showSuccessToast("Playlist added")
                        } else {
                            mainView?.showErrorToast("An error happened while adding your playlist, reason: ${result?.message}")
                        }
                    }
                }
            }
        }
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

        youtubeDL.downloadPlaylist(playlist, { progress, etaInSeconds, line ->
            progressCallback(progress)
        }, {
            runBlocking {
                model.updatePlaylistLastUpdate(id, java.time.LocalDateTime.now().toString())
            }
            refreshPlaylists()
            onSuccess()
        }, {
            runBlocking {
                model.updatePlaylistLastUpdate(id, java.time.LocalDateTime.now().toString())
            }
            refreshPlaylists()
            onFailure()
        })
    }

    override fun downloadPlaylist(id: Int): Unit {
        val playlist = runBlocking {
            return@runBlocking model.loadById(id)
        }

        youtubeDL.downloadPlaylist(playlist, { progress, etaInSeconds, line ->
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

        youtubeDL.downloadPlaylist(playlist, { progress, etaInSeconds, line ->
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