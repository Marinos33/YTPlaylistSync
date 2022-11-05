package com.example.ytplaylistsync.common

class DbResponse(val message: String, rowsAffected: Long) {
    val isSuccess: Boolean

    init {
        isSuccess = rowsAffected > 0
    }
}