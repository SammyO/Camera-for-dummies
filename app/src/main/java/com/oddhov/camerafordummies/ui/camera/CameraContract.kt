package com.oddhov.camerafordummies.ui.camera

import android.graphics.Bitmap
import com.oddhov.camerafordummies.ui.base.BasePresenter
import io.fotoapparat.result.PhotoResult
import io.reactivex.Single

/**
 * Created by sammy on 09/11/2017.
 */
interface CameraContract {
    interface View {
        fun showPermissionView()
        fun showTakePictureView()
        fun showCameraView()
        fun showPhotoResultView()

        fun setResultPhoto(bitmap: Bitmap)

        fun showStoragePermissionRationale()
        fun showCameraPermissionRationale()

        fun showPhotoTakenToast()
        fun showPhotoErrorToast()
        fun showProgressDialog()
        fun hideProgressDialog()
        fun runCounter()

        fun startCamera()
        fun stopCamera()

        fun runMediaScanner(fileLocation: String)
    }

    interface Presenter : BasePresenter {
        fun enablePermissionClicked()
        fun takePhotoClicked()

        fun pictureTaken(result: PhotoResult)
    }

    interface Repo {
        fun rotateBitmap(bitmap: Bitmap): Single<Bitmap>
        fun storeBitmap(bitmap: Bitmap): Single<String>
    }
}