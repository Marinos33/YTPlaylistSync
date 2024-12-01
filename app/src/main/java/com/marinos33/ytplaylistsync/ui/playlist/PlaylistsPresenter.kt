package com.marinos33.ytplaylistsync.ui.playlist

import android.util.Log
import com.marinos33.ytplaylistsync.common.DbResponse
import com.marinos33.ytplaylistsync.services.youtubedl.YoutubeDLService
import com.yausername.youtubedl_android.DownloadProgressCallback
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
               val playlists = model.fetchPlaylists(this@PlaylistsPresenter)

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
                val info = youtubeDL.getInfo(url)
                if (info != null) {
                    var thumbnailUrl: String? = null

                    if (info.thumbnail != null) {
                        thumbnailUrl = info.thumbnail
                    } else if (info.thumbnails != null) {
                        thumbnailUrl = info.thumbnails!![0].url
                    }

                    val now: String = java.time.LocalDateTime.now().toString()

                    val result: DbResponse =
                        model.addPlaylist(info.title.toString(),
                            info.uploader.toString(), now, url, thumbnailUrl)

                    launch(Dispatchers.Main) {
                        if (result.isSuccess) {
                            refreshPlaylists()
                            mainView?.showSuccessToast("Playlist added")
                        } else {
                            mainView?.showErrorToast("An error happened while adding your playlist, reason: ${result.message}")
                        }
                    }
                }
            }
        }
    }

    override fun deletePlaylist(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                val result = model.deletePlaylist(id)

                launch(Dispatchers.Main) {
                    if(result.isSuccess) {
                        refreshPlaylists()
                    } else {
                        mainView?.showErrorToast("An error happened while deleting your playlist, reason: ${result.message}")
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
            try {
                // Fetch playlist data from the model
                val playlist = model.loadById(id)

                // Call the download function on the main thread
                withContext(Dispatchers.Main) {
                    // Ensure that you are passing the correct progress callback
                    youtubeDL.downloadPlaylist(
                        playlist,
                        { progress, etaInSeconds, line ->  // This is a lambda matching (Float, Long, String?) -> Unit
                            // Pass progress update to the callback
                            progressCallback(progress)  // Use the passed callback for progress updates
                        },
                        {
                            // Success case: refresh playlist and call onSuccess
                            refreshPlaylists()
                            onSuccess()
                        },
                        {
                            // Failure case: refresh playlist and call onFailure
                            refreshPlaylists()
                            onFailure()
                        }
                    )
                }

                // Update the last updated timestamp for the playlist
                model.updatePlaylistLastUpdate(id, java.time.LocalDateTime.now().toString())
            } catch (e: Exception) {
                // Handle any unexpected errors
                withContext(Dispatchers.Main) {
                    onFailure()  // Call failure handler
                }
            }
        }
    }


    override fun onFinished(string: String?) {
    }

}