package com.oddhov.camerafordummies.ui.main

import android.graphics.Bitmap
import io.fotoapparat.result.PhotoResult
import io.reactivex.Completable

/**
 * Created by sammy on 09/11/2017.
 */

interface MainContract {
    interface View {
        fun showPermissionView()
        fun showCameraView()

        fun showStoragePermissionRationale()
        fun showCameraPermissionRationale()

        fun showPhotoTakenToast()
        fun showPhotoErrorToast()

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

    interface Repo {
        fun storeBitmap(bitmap: Bitmap): Completable
    }
}