package com.example.ytplaylistsync

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ytplaylistsync.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initYoutubeDL()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
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
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return(super.onOptionsItemSelected(item));
    }

    private fun initYoutubeDL() {
        try {
            YoutubeDL.getInstance().init(this)
            FFmpeg.getInstance().init(this);
            updateYoutubeDL();
        } catch (e: YoutubeDLException) {
            Log.e(TAG, "failed to initialize youtubedl-android", e)
        }
    }

    private fun updateYoutubeDL() {
        val disposable: Disposable = Observable.fromCallable {
            YoutubeDL.getInstance().updateYoutubeDL(application)
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ status ->
                Toast.makeText(
                    this@MainActivity,
                    "update successful",
                    Toast.LENGTH_LONG
                ).show()
         }) { e ->
                if (BuildConfig.DEBUG) Log.e(
                    TAG,
                    "failed to update",
                    e
                )
                Toast.makeText(this@MainActivity, "update failed", Toast.LENGTH_LONG).show()
            }
        compositeDisposable.add(disposable)
    }
}