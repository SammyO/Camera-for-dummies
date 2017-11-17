package com.oddhov.camerafordummies.ui.main.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.oddhov.camerafordummies.R
import com.oddhov.camerafordummies.ui.main.DaggerMainComponent
import com.oddhov.camerafordummies.ui.main.MainContract
import com.oddhov.camerafordummies.ui.main.MainModule
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
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
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

    private var cameraStarted = false;

    // region Activity Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupDi()
        setContentView(R.layout.activity_main)
        setClickListeners()
        presenter.setupView()
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

    override fun storeBitmap(bitmap: Bitmap) {
        val file = createFile()
        if (file == null) {
            Log.e("MainActivity", "file null")
            return
        }

        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            MediaScannerConnection.scanFile(this, arrayOf(file.toString()), null
            ) { _, _ -> }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    // endregion

    // region Helper Methods (UI)
    private fun setupDi() {
        DaggerMainComponent.builder()
                .mainModule(MainModule(this))
                .build()
                .inject(this)
    }

    private fun setClickListeners() {
        btnEnablePermission.setOnClickListener { presenter.enablePermissionClicked() }
        btnTakePicture.setOnClickListener { presenter.pictureTaken(camera.takePicture()) }
//        btnTakePicture.setOnClickListener {
//            var photo = camera.takePicture()
//            photo.saveToFile(createFile())
//        }
    }

    private fun changeScreenState(state: Int) {
        vpMain.displayedChild = vpMain.indexOfChild(vpMain.findViewById(state))
    }
    // endregion

    // region Helper Methods (Photo processing)
    /**
     * Creates a file in external storage and returns the file path
     */
    @SuppressLint("SimpleDateFormat")
    private fun createFile(): File? {
        val currentTimeMillis = System.currentTimeMillis()
        val today = Date(currentTimeMillis)
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
        val title = dateFormat.format(today)
        val fileName = "IMG_$title.png"

        val extStorageDir = Environment.getExternalStorageDirectory()
        if (extStorageDir.canWrite()) {
            val imageDir = File(extStorageDir.path + "/camerafordummies")
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }
            if (imageDir.canWrite()) {
                val file = File(imageDir, fileName)
                if (file.exists())
                    file.delete()
                return file
            }
        }
        return null
    }
    // endregion
}