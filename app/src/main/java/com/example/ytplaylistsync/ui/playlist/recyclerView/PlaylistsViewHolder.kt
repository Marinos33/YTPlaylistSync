package com.example.ytplaylistsync.ui.playlist.recyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ytplaylistsync.R
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity


class PlaylistsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.playlist_name)
    private val author: TextView = itemView.findViewById(R.id.playlist_author)
    private val lastUpdated: TextView = itemView.findViewById(R.id.playlist_last_updated)
    fun bind(playlistDataObject: PlaylistEntity) {
        name.text = playlistDataObject.name
        author.text = playlistDataObject.author
        lastUpdated.text = playlistDataObject.lastUpdated
    }
}