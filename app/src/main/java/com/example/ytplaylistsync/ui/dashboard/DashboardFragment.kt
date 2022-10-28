package com.example.ytplaylistsync.ui.dashboard

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ytplaylistsync.databinding.FragmentDashboardBinding
import com.yausername.youtubedl_android.DownloadProgressCallback
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val compositeDisposable = CompositeDisposable()
    private val processId = "MyDlProcess"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val callback =
        DownloadProgressCallback { progress, etaInSeconds, line ->
            Log.d("YTP", "$progress% (ETA $etaInSeconds seconds)")
        }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        startDownload()

        return root
    }

    private fun startDownload(){
        if (!isStoragePermissionGranted()) {
            Toast.makeText(requireContext(), "grant storage permission and retry", Toast.LENGTH_LONG).show();
            return
        }

        val youtubeDLDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "youtubedl-android"
        )
        val request = YoutubeDLRequest("https://www.youtube.com/watch?v=C0DPdy98e4c")
        request.addOption("-o", youtubeDLDir.absolutePath + "/%(title)s.%(ext)s")

        val disposable: Disposable = Observable.fromCallable {
            YoutubeDL.getInstance().execute(request, processId, callback)
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ youtubeDLResponse ->
                Toast.makeText(
                    requireContext(),
                    "download successful",
                    Toast.LENGTH_LONG
                ).show()
            }) { e ->
                Toast.makeText(
                    requireContext(),
                    "download failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        compositeDisposable.add(disposable)
    }

    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (PermissionChecker.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
        } else {
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}