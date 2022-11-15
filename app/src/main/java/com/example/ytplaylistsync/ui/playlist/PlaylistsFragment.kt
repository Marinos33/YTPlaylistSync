package com.example.ytplaylistsync.ui.playlist

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.example.ytplaylistsync.R
import com.example.ytplaylistsync.databinding.FragmentPlaylistsBinding
import com.example.ytplaylistsync.persistence.entities.PlaylistEntity
import com.example.ytplaylistsync.persistence.repositories.PlaylistRepository
import com.example.ytplaylistsync.services.youtubedl.YoutubeDLService
import com.example.ytplaylistsync.ui.playlist.recyclerView.PlaylistsAdapter
import com.example.ytplaylistsync.ui.playlist.recyclerView.swipeController.SwipeController
import com.example.ytplaylistsync.ui.playlist.recyclerView.swipeController.SwipeControllerActions
import com.yausername.youtubedl_android.YoutubeDL
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import javax.inject.Inject


@AndroidEntryPoint
class PlaylistsFragment : Fragment(), PlaylistsContract.View {

    // creating object of Presenter interface in Contract
    private var presenter: PlaylistsPresenter? = null

    private var _binding: FragmentPlaylistsBinding? = null

    private val binding get() = _binding!!

    private var adapter: PlaylistsAdapter? = null

    @Inject
    lateinit var repository: PlaylistRepository

    @Inject
    lateinit var youtubeDL: YoutubeDLService

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)


        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // instantiating object of Presenter Interface
        presenter = PlaylistsPresenter(this, youtubeDL, PlaylistsModel(repository))

        binding.addButton.setOnClickListener {
            MaterialDialog(requireContext()).show {
                title(R.string.add_dialog_title)
                positiveButton(R.string.add_dialog_positive) { dialog ->
                    val inputField = dialog.getInputField()
                    val text = inputField.text.toString()
                    val isValid = URLUtil.isValidUrl(text)

                    if(isValid){
                        presenter?.addPlaylist(text)
                    }
                }
                negativeButton(R.string.add_dialog_negative)
                input (hintRes = R.string.add_dialog_hint, waitForPositiveButton = false, inputType = InputType.TYPE_TEXT_VARIATION_URI) { dialog, text ->
                    val inputField = dialog.getInputField()
                    val isValid = URLUtil.isValidUrl(text.toString())

                    inputField.error = if (isValid) null else "Must be a valid url!"
                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
                }
            }
        }

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
        adapter = binding.playlistsList.adapter as PlaylistsAdapter?
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter?.getFilter()?.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.getFilter()?.filter(newText)
                return true
            }

        })
    }

    private fun setUpRecyclerView() {
        //attach layout manager
        binding.playlistsList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        //add item decoration for divider
        val itemDecorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(requireContext(), R.drawable.line_divider)?.let { itemDecorator.setDrawable(it) }

        binding.playlistsList.addItemDecoration(itemDecorator)

        binding.playlistsList.let { FastScrollerBuilder(it).useMd2Style().build() }

        var swipeController = SwipeController(requireContext(), object : SwipeControllerActions() {
            override fun onRightClicked(position: Int) {
                //get the element from the list int the given position
                //then delete it from the database and refresh the list
                Log.d("PlaylistsModel", "playlist position: $position")
                val playlist = (binding.playlistsList.adapter as PlaylistsAdapter?)?.getItem(position)
                Log.d("PlaylistsModel", "playlist id: ${playlist?.id}")
                if(playlist != null){
                    playlist.id?.let { presenter?.deletePlaylist(it) }
                    presenter?.refreshPlaylists()
                }
                Log.d("PlaylistsModel", "playlist count: ${binding.playlistsList.adapter?.itemCount}")
            }
        })

        var itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(binding.playlistsList)

        binding.swipeRefresh.setOnRefreshListener {
            presenter?.refreshPlaylists()

            binding.swipeRefresh.isRefreshing = false
        }

        presenter?.refreshPlaylists()
    }

    override fun refreshPlaylists(playlists: List<PlaylistEntity>) {
        val playlistsCopy = ArrayList<PlaylistEntity>().apply {
            addAll(playlists)
        }

        binding.playlistsList.adapter = PlaylistsAdapter(playlistsCopy, presenter!!)
        binding.playlistsList.adapter?.notifyDataSetChanged()
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