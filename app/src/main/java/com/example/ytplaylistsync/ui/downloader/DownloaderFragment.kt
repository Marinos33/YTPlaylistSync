package com.example.ytplaylistsync.ui.downloader

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ytplaylistsync.R
import com.example.ytplaylistsync.databinding.FragmentDownloaderBinding
import com.example.ytplaylistsync.services.youtubedl.YoutubeDLService
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DownloaderFragment : Fragment(), DownloaderContract.View {
    // creating object of Presenter interface in Contract
    var presenter: DownloaderPresenter? = null

    private var _binding: FragmentDownloaderBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var youtubeDL: YoutubeDLService

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        _binding = FragmentDownloaderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // instantiating object of Presenter Interface
        presenter = DownloaderPresenter(this, youtubeDL, DownloaderModel())

        binding.url.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                presenter?.fetchInfo(p0.toString())
            }
        })

        return root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onDestroy()
    }

    override fun setVideoData(title: String, thumbnailUrl: String?) {
        binding.videoTitle.text = title
        if(thumbnailUrl != null) {
            Picasso.get()
                .load(thumbnailUrl)
                .into(binding.thumbnail)
        }
    }
}