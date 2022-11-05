package com.example.ytplaylistsync.persistence.dao

import androidx.room.*
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlist")
    suspend fun getAll(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    suspend fun loadById(playlistId: Int): PlaylistEntity

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(playlist: PlaylistEntity): Long

    @Update
    suspend fun updateUsers(playlist: PlaylistEntity)

    @Delete
    suspend fun delete(playlist: PlaylistEntity): Int
}