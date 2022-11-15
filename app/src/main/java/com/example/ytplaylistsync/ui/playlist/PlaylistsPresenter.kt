package com.example.ytplaylistsync.ui.playlist

import android.util.Log
import android.widget.Toast
import com.example.ytplaylistsync.common.DbResponse
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import com.example.ytplaylistsync.services.youtubedl.YoutubeDLService
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

    override fun deletePlaylist(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                var result = model.deletePlaylist(id)

                launch(Dispatchers.Main) {
                    if(result?.isSuccess == true) {
                        refreshPlaylists()
                    } else {
                        mainView?.showErrorToast("An error happened while deleting your playlist, reason: ${result?.message}")
                    }
                }
            }
        }
    }

    override fun downloadPlaylist(
        id: Int,
        progressCallback: (value: Float) -> Unit,
        onFailure: () -> Unit,
        onSuccess: () -> Unit
    ) {

        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                var playlist = model.loadById(id)

                launch(Dispatchers.Main) {
                    youtubeDL.downloadPlaylist(playlist, { progress, etaInSeconds, line ->
                        progressCallback(progress)
                    }, {
                        refreshPlaylists()
                        onSuccess()
                    }, {
                        refreshPlaylists()
                        onFailure()
                    })
                }

                model.updatePlaylistLastUpdate(id, java.time.LocalDateTime.now().toString())
            }
        }
    }


    override fun onFinished(string: String?) {
    }

}