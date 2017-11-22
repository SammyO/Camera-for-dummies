package com.oddhov.camerafordummies.ui.camera.model

import android.graphics.Bitmap
import com.oddhov.camerafordummies.data.utils.PhotoUtils
import com.oddhov.camerafordummies.ui.camera.CameraContract
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by sammy on 09/11/2017.
 */
class CameraRepo
@Inject
constructor(private val photoUtils: PhotoUtils) : CameraContract.Repo {

    override fun rotateBitmap(bitmap: Bitmap): Single<Bitmap> {
        return photoUtils.rotateBitmap(bitmap)
    }

    override fun storeBitmap(bitmap: Bitmap): Single<String> {
        return photoUtils.storeBitmap(bitmap)
    }
}
