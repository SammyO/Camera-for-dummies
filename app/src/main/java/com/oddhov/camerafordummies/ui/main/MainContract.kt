package com.oddhov.camerafordummies.ui.main

import android.graphics.Bitmap
import io.fotoapparat.result.PhotoResult
import io.reactivex.Single

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
        fun showProgressDialog()
        fun hideProgressDialog()

        fun startCamera()
        fun stopCamera()

        fun runMediaScanner(fileLocation: String)
    }

    interface Presenter {
        fun subscribe()
        fun unsubscribe()

        fun enablePermissionClicked()

        fun pictureTaken(result: PhotoResult)
    }

    interface Repo {
        fun rotateBitmap(bitmap: Bitmap): Single<Bitmap>
        fun storeBitmap(bitmap: Bitmap): Single<String>
    }
}