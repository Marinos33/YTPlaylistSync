package com.example.ytplaylistsync.ui.playlist.recyclerView

import android.Manifest
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.PermissionChecker
import androidx.recyclerview.widget.RecyclerView
import com.example.ytplaylistsync.R
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import com.example.ytplaylistsync.ui.playlist.PlaylistsPresenter
import com.squareup.picasso.Picasso
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PlaylistsViewHolder(itemView: View, var presenter: PlaylistsPresenter) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.playlist_name)
    private val author: TextView = itemView.findViewById(R.id.playlist_author)
    private val lastUpdated: TextView = itemView.findViewById(R.id.playlist_last_updated)
    private val thumbnail: ImageView = itemView.findViewById(R.id.playlist_thumbnail)
    private val downloadButton = itemView.findViewById<ImageView>(R.id.download_button)

    private val compositeDisposable = CompositeDisposable()
    private val processId = "MyDlProcess"

    fun bind(playlistDataObject: PlaylistEntity) {
        name.text = playlistDataObject.name
        author.text = playlistDataObject.author

        val localDateTime = LocalDateTime.parse(playlistDataObject.lastUpdated)
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val output: String = formatter.format(localDateTime)
        lastUpdated.text = "last updated on $output"


        if(playlistDataObject.thumbnail != null) {
            Picasso.get()
                .load(playlistDataObject.thumbnail)
                .into(thumbnail)
        }

        downloadButton.setOnClickListener {
            Log.d("PlaylistsAdapter", "Download button clicked")
            if (!isStoragePermissionGranted()) {
                Toast.makeText(downloadButton.context, "grant storage permission and retry", Toast.LENGTH_LONG).show();
                return@setOnClickListener
            }

            presenter.downloadPlaylist(playlistDataObject.id!!)
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        return (PermissionChecker.checkSelfPermission(downloadButton.context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED && PermissionChecker.checkSelfPermission(downloadButton.context, Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED)
    }
}