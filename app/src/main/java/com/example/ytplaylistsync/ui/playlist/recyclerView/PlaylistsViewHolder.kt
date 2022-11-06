package com.example.ytplaylistsync.ui.playlist.recyclerView

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ytplaylistsync.R
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PlaylistsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.playlist_name)
    private val author: TextView = itemView.findViewById(R.id.playlist_author)
    private val lastUpdated: TextView = itemView.findViewById(R.id.playlist_last_updated)
    private val thumbnail: ImageView = itemView.findViewById(R.id.playlist_thumbnail)
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
    }
}