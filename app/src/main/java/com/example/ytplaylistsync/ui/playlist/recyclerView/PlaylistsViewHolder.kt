package com.example.ytplaylistsync.ui.playlist.recyclerView

import android.Manifest
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.recyclerview.widget.RecyclerView
import com.example.ytplaylistsync.R
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import com.example.ytplaylistsync.ui.playlist.PlaylistsPresenter
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PlaylistsViewHolder(itemView: View, var presenter: PlaylistsPresenter) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.playlist_name)
    private val author: TextView = itemView.findViewById(R.id.playlist_author)
    private val lastUpdated: TextView = itemView.findViewById(R.id.playlist_last_updated)
    private val thumbnail: ImageView = itemView.findViewById(R.id.playlist_thumbnail)
    private val downloadButton = itemView.findViewById<ImageView>(R.id.download_button)
    private val progress = itemView.findViewById<ProgressBar>(R.id.progressBar)

    fun bind(playlistDataObject: PlaylistEntity) {
        name.text = playlistDataObject.name
        author.text = playlistDataObject.author

        val localDateTime = LocalDateTime.parse(playlistDataObject.lastUpdated)
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val output: String = formatter.format(localDateTime)
        lastUpdated.text = "Updated : $output"


        if(playlistDataObject.thumbnail != null) {
            Picasso.get()
                .load(playlistDataObject.thumbnail)
                .into(thumbnail)
        }

        downloadButton.setOnClickListener {
            Log.d("PlaylistsAdapter", "Download button clicked")
            if (!isStoragePermissionGranted()) {
                Toast.makeText(downloadButton.context, "grant storage permission and retry", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            downloadButton.visibility = View.GONE
            progress.visibility = View.VISIBLE

            presenter.downloadPlaylist(playlistDataObject.id!!, { progress ->
                Log.d("YoutubeDL", "$progress%")
            },
            {
                Log.d("YoutubeDL", "Download failed")
                progress.visibility = View.GONE
                downloadButton.visibility = View.VISIBLE
                itemView
            },
            {
                Log.d("YoutubeDL", "Download finished")
                progress.visibility = View.GONE
                downloadButton.visibility = View.VISIBLE
            })
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        return (PermissionChecker.checkSelfPermission(downloadButton.context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED && PermissionChecker.checkSelfPermission(downloadButton.context, Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED)
    }
}