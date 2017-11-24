package com.oddhov.camerafordummies.ui.camera.view

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.oddhov.camerafordummies.CameraForDummiesApplication
import com.oddhov.camerafordummies.R
import com.oddhov.camerafordummies.di.component.DaggerCameraComponent
import com.oddhov.camerafordummies.di.modules.CameraModule
import com.oddhov.camerafordummies.ui.camera.CameraContract
import com.oddhov.camerafordummies.ui.gallery.view.GalleryActivity
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
import kotlinx.android.synthetic.main.activity_camera.progressBar
import kotlinx.android.synthetic.main.activity_camera.vpMain
import kotlinx.android.synthetic.main.layout_camera.cvCamera
import kotlinx.android.synthetic.main.layout_camera.tvOne
import kotlinx.android.synthetic.main.layout_camera.tvThree
import kotlinx.android.synthetic.main.layout_camera.tvTwo
import kotlinx.android.synthetic.main.layout_camera_button.btnTakePicture
import kotlinx.android.synthetic.main.layout_permission_request.btnEnablePermission
import kotlinx.android.synthetic.main.layout_photo_result.ivResultImage
import kotlinx.android.synthetic.main.layout_photo_result.toolbar
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import javax.inject.Inject


/**
 * Created by sammy on 08/11/2017.
 */
class CameraActivity : AppCompatActivity(), CameraContract.View {
    @Inject
    lateinit var presenter: CameraContract.Presenter

    private val camera by lazy {
        Fotoapparat
                .with(this)
                .into(cvCamera)
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
    private lateinit var countDownTimer: CountDownTimer

    // region Activity Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupDi()
        setContentView(R.layout.activity_camera)
        setClickListeners()
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribe()
    }

    override fun onStop() {
        presenter.unsubscribe()
        countDownTimer.cancel()
        super.onStop()
    }
    // endregion

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            presenter.onHomeClicked()
            return true
        }
        return false
    }
    // endregion

    // region interface MainContact.View
    override fun showPermissionView() {
        changeScreenState(R.id.layoutCameraPermission)
    }

    override fun showTakePictureView() {
        changeScreenState(R.id.layoutCameraButton)
    }

    override fun showCameraView() {
        changeScreenState(R.id.layoutCamera)
    }

    override fun showPhotoResultView() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        changeScreenState(R.id.layoutPhotoResult)
    }

    override fun setResultPhoto(bitmap: Bitmap) {
        ivResultImage.setImageBitmap(bitmap)
        ivResultImage.rotation = 90f
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

    override fun showPhotoTakenToast() {
        toast(R.string.toast_photo_taken_success)
    }

    override fun showPhotoErrorToast() {
        toast(R.string.toast_photo_taken_error)
    }

    override fun hideProgressDialog() {
        progressBar.visibility = View.GONE
    }

    override fun showProgressDialog() {
        progressBar.visibility = View.VISIBLE
    }

    override fun runCounter() {
        countDownTimer = object : CountDownTimer(4000, 500) {
            override fun onTick(millisUntilFinished: Long) {
                when (Math.round(millisUntilFinished.toFloat() / 1000.0f)) {
                    3 -> showCounterOne()
                    2 -> showCounterTwo()
                    1 -> showCounterThree()
                }
            }
            override fun onFinish() {
                hideCounter()
                presenter.pictureTaken(camera.takePicture())
            }
        }.start()
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

    override fun openGalleryActivity() {
        startActivity(Intent(this, GalleryActivity::class.java))
        finish()
    }

    override fun runMediaScanner(fileLocation: String) {
        MediaScannerConnection.scanFile(this, arrayOf(fileLocation), null
        ) { _, _ -> }
    }
    // endregion

    // region Helper Methods (UI)
    private fun setupDi() {
        DaggerCameraComponent.builder()
                .applicationComponent((application as CameraForDummiesApplication).getComponent())
                .cameraModule(CameraModule(this))
                .build()
                .inject(this)
    }

    private fun setClickListeners() {
        btnEnablePermission.setOnClickListener { presenter.enablePermissionClicked() }
        btnTakePicture.setOnClickListener { presenter.takePhotoClicked() }
    }

    private fun changeScreenState(state: Int) {
        vpMain.displayedChild = vpMain.indexOfChild(vpMain.findViewById(state))
    }

    private fun showCounterOne() {
        tvOne.visibility = View.VISIBLE
        tvTwo.visibility = View.INVISIBLE
        tvThree.visibility = View.INVISIBLE
    }

    private fun showCounterTwo() {
        tvOne.visibility = View.INVISIBLE
        tvTwo.visibility = View.VISIBLE
        tvThree.visibility = View.INVISIBLE
    }

    private fun showCounterThree() {
        tvOne.visibility = View.INVISIBLE
        tvTwo.visibility = View.INVISIBLE
        tvThree.visibility = View.VISIBLE
    }

    private fun hideCounter() {
        tvOne.visibility = View.GONE
        tvTwo.visibility = View.GONE
        tvThree.visibility = View.GONE
    }
    // endregion
}