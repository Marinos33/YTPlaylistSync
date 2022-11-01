package com.example.ytplaylistsync.persistence.dao

import androidx.room.*
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlist")
    fun getAll(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist WHERE id IN (:playlistId)")
    fun loadById(playlistId: Int): PlaylistEntity

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(playlist: PlaylistEntity)

    @Update
    fun updateUsers(playlist: PlaylistEntity)

    @Delete
    fun delete(playlist: PlaylistEntity)
}