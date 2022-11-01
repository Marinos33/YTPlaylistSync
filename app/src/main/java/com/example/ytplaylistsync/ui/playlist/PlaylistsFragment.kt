package com.example.ytplaylistsync.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ytplaylistsync.R
import com.example.ytplaylistsync.databinding.FragmentPlaylistsBinding
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import com.example.ytplaylistsync.ui.playlist.recyclerView.PlaylistsAdapter
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import java.util.*
import kotlin.collections.ArrayList


class PlaylistsFragment : Fragment(), PlaylistsContract.View {

    // creating object of Presenter interface in Contract
    var presenter: PlaylistsPresenter? = null

    private var _binding: FragmentPlaylistsBinding? = null

    private val binding get() = _binding!!

    private var adapter: PlaylistsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)


        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // instantiating object of Presenter Interface
        presenter = PlaylistsPresenter(this, PlaylistsModel())

        setUpRecyclerView()
        setUpSearchView()

        return root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onDestroy()
    }

    private fun setUpSearchView() {
        binding?.searchBar?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter?.getFilter()?.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.getFilter()?.filter(newText);
                return true
            }

        })
    }

    private fun setUpRecyclerView() {
        //attach layout manager
        binding?.playlistsList?.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        //add item decoration for divider
        val itemDecorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.line_divider)?.let { itemDecorator.setDrawable(it) }
        binding?.playlistsList?.addItemDecoration(itemDecorator)

        //create a copy of city list
        val playlists = presenter?.fetchPlaylists()
        val playlistsCopy = ArrayList<PlaylistEntity>().apply {
            if (playlists != null) {
                addAll(playlists)
            }
        }

        //attach adapter to list
        adapter = PlaylistsAdapter(playlistsCopy)
        binding?.playlistsList?.adapter = adapter

        binding?.playlistsList?.let { FastScrollerBuilder(it).useMd2Style().build() };

        binding.swipeRefresh.setOnRefreshListener {

            //wait 5 seconds
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    binding.swipeRefresh.isRefreshing = false

                    // on below line we are notifying adapter
                    // that data has changed in recycler view.
                    //adapter?.notifyDataSetChanged()
                }
            }, 5000)
        }
    }
}