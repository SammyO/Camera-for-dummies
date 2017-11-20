package com.oddhov.camerafordummies.ui.main.view

import android.media.MediaScannerConnection
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.oddhov.camerafordummies.CameraForDummiesApplication
import com.oddhov.camerafordummies.R
import com.oddhov.camerafordummies.di.component.DaggerActivityComponent
import com.oddhov.camerafordummies.di.modules.MainModule
import com.oddhov.camerafordummies.ui.main.MainContract
import io.fotoapparat.Fotoapparat
import io.fotoapparat.log.Loggers.logcat
import io.fotoapparat.log.Loggers.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.parameter.selector.FocusModeSelectors.autoFocus
import io.fotoapparat.parameter.selector.FocusModeSelectors.continuousFocus
import io.fotoapparat.parameter.selector.FocusModeSelectors.fixed
import io.fotoapparat.parameter.selector.LensPositionSelectors.back
import io.fotoapparat.parameter.selector.Selectors.firstAvailable
import io.fotoapparat.parameter.selector.SizeSelectors.biggestSize
import kotlinx.android.synthetic.main.activity_main.vpMain
import kotlinx.android.synthetic.main.layout_camera.btnTakePicture
import kotlinx.android.synthetic.main.layout_camera.camera_view
import kotlinx.android.synthetic.main.layout_permission_request.btnEnablePermission
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import javax.inject.Inject


/**
 * Created by sammy on 08/11/2017.
 */

class MainActivity : AppCompatActivity(), MainContract.View {
    @Inject
    lateinit var presenter: MainContract.Presenter

    private val camera by lazy {
        Fotoapparat
                .with(this)
                .into(camera_view)
                .previewScaleType(ScaleType.CENTER_CROP)
                .photoSize(biggestSize())
                .lensPosition(back())
                .focusMode(firstAvailable(
                        continuousFocus(),
                        autoFocus(),
                        fixed()
                ))
                .logger(loggers(
                        logcat()
                ))
                .build()
    }

    private var cameraStarted = false

    // region Activity Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupDi()
        setContentView(R.layout.activity_main)
        setClickListeners()
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribe()
    }

    override fun onStop() {
        presenter.unsubscribe()
        super.onStop()
    }

    // region interface MainContact.View
    override fun showPermissionView() {
        changeScreenState(R.id.layoutCameraPermission)
    }

    override fun showCameraView() {
        changeScreenState(R.id.layoutCamera)
    }

    override fun showStoragePermissionRationale() {
        alert(R.string.alert_allow_camera_permission_message,
                R.string.alert_allow_camera_permission_title) {
            positiveButton(R.string.alert_allow_storage_permission_message, {})
            negativeButton(R.string.word_cancel, {})
        }.show()
    }

    override fun showCameraPermissionRationale() {
        alert(R.string.alert_allow_camera_permission_message,
                R.string.alert_allow_camera_permission_title) {
            positiveButton(R.string.alert_allow_permission_app_info, {})
            negativeButton(R.string.word_cancel, {})
        }.show()
    }

    override fun showPhotoTakenToast(fileLocation: String) {
        MediaScannerConnection.scanFile(this, arrayOf(fileLocation), null
        ) { _, _ -> }
        toast(R.string.toast_photo_taken_success)
    }

    override fun showPhotoErrorToast() {
        toast(R.string.toast_photo_taken_error)
    }

    override fun startCamera() {
        if (!cameraStarted) {
            camera.start()
            cameraStarted = true
        }
    }

    override fun stopCamera() {
        if (cameraStarted) {
            camera.stop()
        }
        cameraStarted = false
    }
    // endregion

    // region Helper Methods (UI)
    private fun setupDi() {
        DaggerActivityComponent.builder()
                .applicationComponent((application as CameraForDummiesApplication).getComponent())
                .mainModule(MainModule(this))
                .build()
                .inject(this)
    }

    private fun setClickListeners() {
        btnEnablePermission.setOnClickListener { presenter.enablePermissionClicked() }
        btnTakePicture.setOnClickListener { presenter.pictureTaken(camera.takePicture()) }
    }

    private fun changeScreenState(state: Int) {
        vpMain.displayedChild = vpMain.indexOfChild(vpMain.findViewById(state))
    }
    // endregion
}