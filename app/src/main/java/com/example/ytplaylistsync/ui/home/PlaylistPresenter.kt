package com.example.ytplaylistsync.ui.home

class PlaylistPresenter(
    private var mainView: PlaylistContract.View?,
    private val model: PlaylistContract.Model) : PlaylistContract.Presenter,
    PlaylistContract.Model.OnFinishedListener {

    // operations to be performed
    // on button click
    override fun onButtonClick() {
        if (mainView != null) {
            mainView!!.showProgress()
        }
        model.getNextCourse(this)
    }

    override fun onDestroy() {
        mainView = null
    }

    // method to return the string
    // which will be displayed in the
    // Course Detail TextView
    override fun onFinished(string: String?) {
        if (mainView != null) {
            mainView!!.setString(string)
            mainView!!.hideProgress()
        }
    }

}