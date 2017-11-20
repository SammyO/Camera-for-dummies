package com.oddhov.camerafordummies.ui.main.presenter

import android.Manifest
import com.oddhov.camerafordummies.data.extentions.applySchedulers
import com.oddhov.camerafordummies.ui.main.MainContract
import com.tbruyelle.rxpermissions2.RxPermissions
import io.fotoapparat.result.PhotoResult
import io.fotoapparat.result.adapter.rxjava2.SingleAdapter
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by sammy on 09/11/2017.
 */

class MainPresenter
@Inject
constructor(private val view: MainContract.View, private val repo: MainContract.Repo,
            private val rxPermissions: RxPermissions) : MainContract.Presenter {

    override fun subscribe() {
        checkPermissions()
    }

    override fun unsubscribe() {

    }

    override fun enablePermissionClicked() {
        rxPermissions.requestEach(Manifest.permission.CAMERA)
                .subscribe({ permission ->
                    if (permission.granted) {
                        rxPermissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .all { it.granted }
                                .subscribe { granted ->
                                    if (granted) enableCameraView()
                                    else view.showStoragePermissionRationale()
                                }

                    } else if (!permission.shouldShowRequestPermissionRationale) {
                        view.showCameraPermissionRationale()
                    }
                })
    }

    override fun pictureTaken(result: PhotoResult) {
        result
                .toBitmap()
                .adapt(SingleAdapter.toSingle())
                .flatMap {
                    repo.storeBitmap(it.bitmap)
                }
                .applySchedulers()
                .subscribe({
                    view.showPhotoTakenToast(it)
                }, {
                    Timber.e(it)
                    view.showPhotoErrorToast()
                })
    }

    private fun checkPermissions() {
        rxPermissions.requestEach(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .all({ it.granted })
                .subscribe { granted ->
                    if (granted) enableCameraView()
                    else enablePermissionsView()
                }
    }

    private fun enableCameraView() {
        view.showCameraView()
        view.startCamera()
    }

    private fun enablePermissionsView() {
        view.showPermissionView()
        view.stopCamera()
    }
}