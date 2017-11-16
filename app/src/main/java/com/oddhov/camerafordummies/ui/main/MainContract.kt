package com.oddhov.camerafordummies.ui.main

import io.fotoapparat.result.PhotoResult

/**
 * Created by sammy on 09/11/2017.
 */

interface MainContract {
    interface View {
        fun showPermissionView()
        fun showCameraView()

        fun showStoragePermissionRationale()
        fun showCameraPermissionRationale()

        fun startCamera()
        fun stopCamera()
    }

    interface Presenter {
        fun subscribe()
        fun unsubscribe()

        fun setupView()
        fun enablePermissionClicked()

        fun pictureTaken(result: PhotoResult)
    }

    interface Repo

}