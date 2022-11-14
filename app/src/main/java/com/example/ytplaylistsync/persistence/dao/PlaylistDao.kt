package com.example.ytplaylistsync.persistence.dao

import androidx.room.*
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlist ORDER BY playlist_name ASC")
    suspend fun getAll(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    suspend fun loadById(playlistId: Int): PlaylistEntity

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(playlist: PlaylistEntity): Long

    //update playlist lastUpdateted whre id = playlistId
    @Query("UPDATE playlist SET playlist_last_updated = :lastUpdated WHERE id = :playlistId")
    suspend fun updatePlaylistLastUpdate(playlistId: Int, lastUpdated: String)

    @Delete
    suspend fun delete(playlist: PlaylistEntity): Int
}