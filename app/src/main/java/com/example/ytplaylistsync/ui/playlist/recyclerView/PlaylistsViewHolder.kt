package com.example.ytplaylistsync.ui.playlist.recyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ytplaylistsync.R
import com.example.ytplaylistsync.domain.entities.PlaylistEntity


class PlaylistsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val playlistName: TextView = itemView.findViewById(R.id.city_name)
    private val playlistCode: TextView = itemView.findViewById(R.id.city_code)
    fun bind(cityDataObject: PlaylistEntity) {
        playlistName.text = cityDataObject.cityName
        playlistCode.text = cityDataObject.cityCode
    }
}