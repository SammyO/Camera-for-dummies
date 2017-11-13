package com.oddhov.camerafordummies.ui.main.presenter

import android.Manifest
import com.oddhov.camerafordummies.ui.main.MainContract
import com.tbruyelle.rxpermissions2.RxPermissions
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

    override fun setupView() {
        rxPermissions.requestEach(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .all({it.granted})
                .subscribe { granted ->
                    if (granted) view.showCameraView()
                    else view.showPermissionView()
                }
    }

    override fun enablePermissionClicked() {
        rxPermissions.requestEach(Manifest.permission.CAMERA)
                .subscribe({ permission ->
                    if (permission.granted) {
                        rxPermissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .all{it.granted}
                                .subscribe{ granted ->
                                    if (granted) view.showCameraView()
                                    else view.showStoragePermissionRationale()
                                }

                    } else if (!permission.shouldShowRequestPermissionRationale) {
                        view.showCameraPermissionRationale()
                    }
                })
    }

    private fun checkPermissions() {
        if (rxPermissions.isGranted(Manifest.permission.CAMERA) &&
                rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE))
            view.showCameraView()
    }
}