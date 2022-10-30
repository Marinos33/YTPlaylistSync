package com.example.ytplaylistsync.ui.downloader

class DownloaderPresenter(
    private var mainView: DownloaderContract.View?,
    private val model: DownloaderContract.Model) : DownloaderContract.Presenter,
    DownloaderContract.Model.OnFinishedListener {

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