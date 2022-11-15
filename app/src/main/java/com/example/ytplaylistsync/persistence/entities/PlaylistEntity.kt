package com.example.ytplaylistsync.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "playlist", indices = [Index(value = ["playlist_url"], unique = true)])
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "playlist_name") val name: String,
    @ColumnInfo(name = "playlist_author") val author: String,
    @ColumnInfo(name = "playlist_last_updated") val lastUpdated: String,
    @ColumnInfo(name = "playlist_url") val url: String,
    @ColumnInfo(name = "playlist_thumbnail") val thumbnail: String?
    )