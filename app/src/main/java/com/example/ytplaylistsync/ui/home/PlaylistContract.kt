package com.example.ytplaylistsync.ui.home

interface PlaylistContract {
    interface View {
        // method to display progress bar
        // when next random course details
        // is being fetched
        fun showProgress()

        // method to hide progress bar
        // when next random course details
        // is being fetched
        fun hideProgress()

        // method to set random
        // text on the TextView
        fun setString(string: String?)
    }

    interface Model {
        // nested interface to be
        interface OnFinishedListener {
            // function to be called
            // once the Handler of Model class
            // completes its execution
            fun onFinished(string: String?)
        }

        fun getNextCourse(onFinishedListener: OnFinishedListener?)
    }

    interface Presenter {
        // method to be called when
        // the button is clicked
        fun onButtonClick()

        // method to destroy
        // lifecycle of MainActivity
        fun onDestroy()
    }
}