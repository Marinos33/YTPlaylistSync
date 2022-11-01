package com.example.ytplaylistsync.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*data class PlaylistEntity(
    @Expose @SerializedName("playlist_name") val name: String,
    @Expose @SerializedName("playlist_author") val author: String,
    @Expose @SerializedName("playlist_last_updated") val lastUpdated: String
)*/

@Entity(tableName = "playlist", indices = [Index(value = ["playlist_url"], unique = true)])
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "playlist_name") val name: String?,
    @ColumnInfo(name = "playlist_author") val author: String?,
    @ColumnInfo(name = "playlist_last_updated") val lastUpdated: String?,
    @ColumnInfo(name = "playlist_url") val url: String?,
    @ColumnInfo(name = "playlist_thumbnail") val thumbnail: String?
    )