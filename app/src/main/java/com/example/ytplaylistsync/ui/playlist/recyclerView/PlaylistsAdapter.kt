package com.example.ytplaylistsync.ui.playlist.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ytplaylistsync.R
import com.example.ytplaylistsync.domain.entities.PlaylistEntity
import java.util.*
import kotlin.collections.ArrayList

class PlaylistsAdapter(private var cityDataList: ArrayList<PlaylistEntity>) :
    RecyclerView.Adapter<PlaylistsViewHolder>() {

    // Create a copy of localityList that is not a clone
    // (so that any changes in localityList aren't reflected in this list)
    val initialCityDataList = ArrayList<PlaylistEntity>().apply {
        cityDataList?.let { addAll(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return PlaylistsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.playlist_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(cityDataList[position])
    }

    override fun getItemCount(): Int {
        return cityDataList.size
    }

    fun getFilter(): Filter {
        return cityFilter
    }

    private val cityFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<PlaylistEntity> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                initialCityDataList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().toLowerCase()
                initialCityDataList.forEach {
                    if (it.cityName.toLowerCase(Locale.ROOT).contains(query)) {
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
                cityDataList.clear()
                cityDataList.addAll(results.values as ArrayList<PlaylistEntity>)
                notifyDataSetChanged()
            }
        }
    }
}