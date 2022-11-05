package com.example.ytplaylistsync.ui.playlist.recyclerView.swipeController

abstract class SwipeControllerActions {
    fun onLeftClicked(position: Int) {}
    open fun onRightClicked(position: Int) {}
}