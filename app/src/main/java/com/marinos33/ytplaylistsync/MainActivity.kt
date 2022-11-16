package com.marinos33.ytplaylistsync

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.marinos33.ytplaylistsync.databinding.ActivityMainBinding
import com.marinos33.ytplaylistsync.services.preferences.PrefsManager
import com.marinos33.ytplaylistsync.services.youtubedl.YoutubeDLService
import com.marinos33.ytplaylistsync.ui.settings.SettingsActivity
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

        requestPermissions()

        initYoutubeDL()

        PrefsManager.init(this)

        val actionBar = supportActionBar
        val color = ColorDrawable(resources.getColor(R.color.colorPrimary))
        actionBar?.setBackgroundDrawable(color)

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
                Log.d("main", "YoutubeDL updated")
            }, {
                Toasty.error(this@MainActivity, "failed to initialize the app. Things probably wont go nicely", Toast.LENGTH_LONG).show()
            })
        } catch (e: YoutubeDLException) {
            Toasty.error(this@MainActivity, "failed to initialize the app. Things probably wont go nicely", Toast.LENGTH_LONG).show()
        }
    }

    private fun requestPermissions() {
        if(!checkPermission()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                try {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                    val uri = Uri.fromParts("package", this.packageName, null)
                    intent.data = uri
                    storageActivityResultLauncher.launch(intent)
                }
                catch (e: Exception){
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    storageActivityResultLauncher.launch(intent)
                }
            }
            else{
                //Android is below 11(R)
                ActivityCompat.requestPermissions(this,
                    arrayOf(permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE),
                    100
                )
            }
        }
    }

    private fun checkPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            Environment.isExternalStorageManager()
        }
        else{
            //Android is below 11(R)
            val write = ContextCompat.checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this, permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        //here we will handle the result of our intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            if (!Environment.isExternalStorageManager()){
                //Manage External Storage Permission is granted
                Toasty.info(this, "Manage External Storage Permission is denied....", Toast.LENGTH_LONG).show()
            }
        }
    }
}