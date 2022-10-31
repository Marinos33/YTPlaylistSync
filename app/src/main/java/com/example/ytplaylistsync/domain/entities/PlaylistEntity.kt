package com.example.ytplaylistsync.domain.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlaylistEntity(
    @Expose @SerializedName("playlist_name") val name: String,
    @Expose @SerializedName("playlist_author") val author: String,
    @Expose @SerializedName("playlist_last_updated") val lastUpdated: String
)