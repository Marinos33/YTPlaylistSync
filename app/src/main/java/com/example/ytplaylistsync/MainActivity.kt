package com.example.ytplaylistsync

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ytplaylistsync.databinding.ActivityMainBinding
import com.example.ytplaylistsync.services.preferences.PrefsManager
import com.example.ytplaylistsync.services.youtubedl.YoutubeDLService
import com.example.ytplaylistsync.ui.settings.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.yausername.youtubedl_android.YoutubeDLException
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var youtubeDL: YoutubeDLService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initYoutubeDL()

        PrefsManager.init(this)

        val navView: BottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_playlists, R.id.navigation_downloader))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_UNLABELED
        navView.itemIconSize = 80
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return(super.onOptionsItemSelected(item))
    }

    private fun initYoutubeDL() {
        try {
            youtubeDL.init(this)
            youtubeDL.updateYoutubeDL(application, {
                Log.d(TAG, "YoutubeDL updated")
            }, {
                Toasty.error(this@MainActivity, "failed to initialize the app. Things probably wont go nicely", Toast.LENGTH_LONG).show()
            })
        } catch (e: YoutubeDLException) {
            Log.e(TAG, "failed to initialize youtubedl-android", e)
        }
    }
}