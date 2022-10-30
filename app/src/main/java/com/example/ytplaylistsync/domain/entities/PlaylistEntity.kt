package com.example.ytplaylistsync.domain.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlaylistEntity(
    @Expose @SerializedName("city_name") val cityName: String,
    @Expose @SerializedName("city_code") val cityCode: String
)