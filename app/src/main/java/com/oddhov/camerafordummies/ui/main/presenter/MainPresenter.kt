package com.oddhov.camerafordummies.ui.main.presenter

import android.Manifest
import android.annotation.SuppressLint
import android.os.Environment
import android.util.Log
import com.oddhov.camerafordummies.ui.main.MainContract
import com.tbruyelle.rxpermissions2.RxPermissions
import io.fotoapparat.result.PhotoResult
import io.fotoapparat.result.adapter.rxjava2.SingleAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
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
                    if (granted) enableCameraView()
                    else enablePermissionsView()
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    view.storeBitmap(it.bitmap)
                } , {
                    Log.e("MainPresenter", it.toString())
                })
    }

    private fun checkPermissions() {
//        if (rxPermissions.isGranted(Manifest.permission.CAMERA) &&
//                rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE))
//            view.showCameraView()
    }

    private fun enableCameraView() {
        view.showCameraView()
        view.startCamera()
    }

    private fun enablePermissionsView() {
        view.showPermissionView()
        view.stopCamera()
    }

    /**
     * Creates a file in external storage and returns the file path
     */
    @SuppressLint("SimpleDateFormat")
    private fun createFile(): File? {
        val currentTimeMillis = System.currentTimeMillis()
        val today = Date(currentTimeMillis)
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
        val title = dateFormat.format(today)
        val fileName = "IMG_" + title + ".png"

        var imageDir: File? = null
        val extStorageDir = Environment.getExternalStorageDirectory()
        if (extStorageDir.canWrite()) {
            imageDir = File(extStorageDir.path + "/cropping/", fileName)
        }
        if (imageDir != null) {
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }
            if (imageDir.canWrite()) {
                return imageDir;
            } else {
                Log.e("MainActivity", "Can't write to file")
            }
        }
        return null
    }
}