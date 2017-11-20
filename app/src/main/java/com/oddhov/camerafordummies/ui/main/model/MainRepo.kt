package com.oddhov.camerafordummies.ui.main.model

import android.graphics.Bitmap
import com.oddhov.camerafordummies.data.extentions.applySchedulers
import com.oddhov.camerafordummies.data.utils.PhotoUtils
import com.oddhov.camerafordummies.ui.main.MainContract
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by sammy on 09/11/2017.
 */

class MainRepo
@Inject
constructor(private val photoUtils: PhotoUtils) : MainContract.Repo {

    override fun storeBitmap(bitmap: Bitmap): Single<String> {
        return photoUtils.storeBitmap(bitmap)
    }
}
