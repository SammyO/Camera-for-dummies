package com.oddhov.camerafordummies.ui.camera.presenter

import android.Manifest
import com.oddhov.camerafordummies.ui.camera.CameraContract
import com.tbruyelle.rxpermissions2.RxPermissions
import io.fotoapparat.result.adapter.rxjava2.SingleAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by sammy on 09/11/2017.
 */
class CameraPresenter
@Inject
constructor(private val view: CameraContract.View, private val repo: CameraContract.Repo,
            private val rxPermissions: RxPermissions) : CameraContract.Presenter {

    private val disposables = CompositeDisposable()

    override fun subscribe() {
        checkPermissions()
    }

    override fun unsubscribe() {
        disposables.clear()
    }

    override fun enablePermissionClicked() {
        disposables.add(
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
        )
    }

    override fun takePhotoClicked() {
        view.showCameraView()
        view.runCounter()
    }

    override fun onHomeClicked() {
        view.openGalleryActivity()
    }

    override fun pictureTaken(result: io.fotoapparat.result.PhotoResult) {
        view.showProgressDialog()
        view.showPhotoResultView()

        disposables.add(
            result
                    .toBitmap()
                    .adapt(SingleAdapter.toSingle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map {
                        view.setResultPhoto(it.bitmap)
                        it
                    }
                    .observeOn(Schedulers.io())
                    .flatMap {
                        repo.rotateBitmap(it.bitmap)
                    }
                    .flatMap {
                        repo.storeBitmap(it)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally {
                        view.hideProgressDialog() }
                    .subscribe({
                        view.runMediaScanner(it)
                        view.showPhotoTakenToast()
                    }, {
                        Timber.e(it)
                        view.showPhotoErrorToast()
                    })
        )
    }

    private fun checkPermissions() {
        disposables.add(
            rxPermissions.requestEach(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .all({ it.granted })
                    .subscribe { granted ->
                        if (granted) enableCameraView()
                        else enablePermissionsView()
                    }
        )
    }

    private fun enableCameraView() {
        view.showTakePictureView()
        view.startCamera()
    }

    private fun enablePermissionsView() {
        view.showPermissionView()
        view.stopCamera()
    }
}