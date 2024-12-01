package com.marinos33.ytplaylistsync.ui.playlist.recyclerView

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.marinos33.ytplaylistsync.R
import com.marinos33.ytplaylistsync.persistence.entities.PlaylistEntity
import com.marinos33.ytplaylistsync.ui.playlist.PlaylistsPresenter
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PlaylistsViewHolder(itemView: View, private var presenter: PlaylistsPresenter) : RecyclerView.ViewHolder(itemView) {
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
        lastUpdated.text = itemView.context.getString(R.string.updatedOn, output)

        if(playlistDataObject.thumbnail != null) {
            Picasso.get()
                .load(playlistDataObject.thumbnail)
                .into(thumbnail)
        }

        downloadButton.setOnClickListener {
            if (!checkPermission()) {
                Toasty.info(downloadButton.context, "grant storage permission and retry", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            downloadButton.visibility = View.GONE
            progress.visibility = View.VISIBLE

            presenter.downloadPlaylist(playlistDataObject.id!!, { progress ->
                //Log.d("YoutubeDL", "$progress%")
            },
            {
                progress.visibility = View.GONE
                downloadButton.visibility = View.VISIBLE
                //log error
                //Log.e("YoutubeDL", it.toString())
                Toasty.error(downloadButton.context, "Something went wrong while downloading your playlist. Things may have worked out perfectly... or not", Toast.LENGTH_LONG).show()
            },
            {
                progress.visibility = View.GONE
                downloadButton.visibility = View.VISIBLE
                Toasty.success(downloadButton.context, "Playlist downloaded successfully", Toast.LENGTH_LONG).show()
            })
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun checkPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            Environment.isExternalStorageManager()
        }
        else{
            //Android is below 11(R)
            val write = ContextCompat.checkSelfPermission(downloadButton.context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(downloadButton.context, Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }
}