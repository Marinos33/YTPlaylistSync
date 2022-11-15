package com.example.ytplaylistsync.services.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


//create singleton object with context in constructor
object PrefsManager {
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }

    fun setBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }
}
