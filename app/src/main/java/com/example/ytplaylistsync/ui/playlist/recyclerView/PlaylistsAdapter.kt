package com.example.ytplaylistsync.ui.playlist.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.example.ytplaylistsync.R
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import com.example.ytplaylistsync.ui.playlist.PlaylistsPresenter
import java.util.*
import kotlin.collections.ArrayList

class PlaylistsAdapter(private var playlists: ArrayList<PlaylistEntity>, private var presenter: PlaylistsPresenter) :
    RecyclerView.Adapter<PlaylistsViewHolder>() {

    // Create a copy of localityList that is not a clone
    // (so that any changes in localityList aren't reflected in this list)
    val initialPlaylistDataList = ArrayList<PlaylistEntity>().apply {
        playlists.let { addAll(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return PlaylistsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.playlist_item, parent, false), presenter
        )
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    //function to get teh item in the given position
    fun getItem(position: Int): PlaylistEntity {
        return playlists[position]
    }

    fun getFilter(): Filter {
        return playlistFilter
    }

    private val playlistFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<PlaylistEntity> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                initialPlaylistDataList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().lowercase(Locale.ROOT)
                initialPlaylistDataList.forEach {
                    if (it.name.lowercase(Locale.ROOT).contains(query) || it.url.lowercase(Locale.ROOT)
                            .contains(query)
                    ) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is ArrayList<*>) {
                playlists.clear()
                playlists.addAll(results.values as ArrayList<PlaylistEntity>)
                notifyDataSetChanged()
            }
        }
    }
}