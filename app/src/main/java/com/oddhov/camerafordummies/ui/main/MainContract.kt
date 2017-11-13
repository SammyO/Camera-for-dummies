package com.oddhov.camerafordummies.ui.main

/**
 * Created by sammy on 09/11/2017.
 */

interface MainContract {
    interface View {
        fun showPermissionView()
        fun showCameraView()

        fun showStoragePermissionRationale()
        fun showCameraPermissionRationale()
    }

    interface Presenter {
        fun subscribe()
        fun unsubscribe()
        fun setupView()
        fun enablePermissionClicked()
    }

    interface Repo

}