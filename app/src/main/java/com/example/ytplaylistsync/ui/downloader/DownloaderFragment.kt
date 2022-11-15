package com.example.ytplaylistsync.ui.downloader

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ytplaylistsync.databinding.FragmentDownloaderBinding
import com.example.ytplaylistsync.services.youtubedl.YoutubeDLService
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
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
                              savedInstanceState: Bundle?): View {
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

        binding.downloadButton.setOnClickListener {
            presenter?.downloadVideo(binding.url.text.toString(), binding.commands.text.toString())
        }

        binding.stopButton.setOnClickListener {
            presenter?.stopDownload()
        }

        return root
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

    override fun setProgress(progress: Int) {
        binding.progressBar.progress = progress
    }

    override fun showProgress() {
        binding.progressBar.visibility = ProgressBar.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar.visibility = ProgressBar.GONE
    }

    override fun showSuccessToast(message: String) {
        Toasty.success(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorToast(message: String) {
        Toasty.error(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun showInfoToast(message: String) {
        Toasty.info(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}