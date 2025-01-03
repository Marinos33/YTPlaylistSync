package com.marinos33.ytplaylistsync.common

class DbResponse(val message: String, rowsAffected: Long) {
    val isSuccess: Boolean = rowsAffected > 0
}