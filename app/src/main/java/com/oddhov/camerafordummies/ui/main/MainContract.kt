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

        fun showPhotoTakenToast(fileLocation: String)
        fun showPhotoErrorToast()

        fun startCamera()
        fun stopCamera()
    }

    interface Presenter {
        fun subscribe()
        fun unsubscribe()

        fun enablePermissionClicked()

        fun pictureTaken(result: PhotoResult)
    }

    interface Repo {
        fun storeBitmap(bitmap: Bitmap): Single<String>
    }
}