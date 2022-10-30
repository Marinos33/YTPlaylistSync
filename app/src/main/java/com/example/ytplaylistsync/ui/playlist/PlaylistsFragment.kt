package com.example.ytplaylistsync.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ytplaylistsync.R


class PlaylistsFragment : Fragment(), PlaylistContract.View {
    // creating object of TextView class
    private var textView: TextView? = null

    // creating object of Button class
    private var button: Button? = null

    // creating object of ProgressBar class
    private var progressBar: ProgressBar? = null

    // creating object of Presenter interface in Contract
    var presenter: PlaylistPresenter? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_playlist, container, false)
        // assigning ID of the TextView
        textView = view.findViewById(R.id.textView)

        // assigning ID of the Button
        button = view.findViewById(R.id.button)

        // assigning ID of the ProgressBar
        progressBar = view.findViewById(R.id.progressBar)

        // instantiating object of Presenter Interface
        presenter = PlaylistPresenter(this, PlaylistModel())

        return view
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onDestroy()
    }
}