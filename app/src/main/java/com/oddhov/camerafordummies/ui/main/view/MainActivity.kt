package com.oddhov.camerafordummies.ui.main.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.oddhov.camerafordummies.R
import com.oddhov.camerafordummies.ui.main.DaggerMainComponent
import com.oddhov.camerafordummies.ui.main.MainContract
import com.oddhov.camerafordummies.ui.main.MainModule
import kotlinx.android.synthetic.main.activity_main.vpMain
import kotlinx.android.synthetic.main.layout_permission_request.btnEnablePermission
import org.jetbrains.anko.alert
import javax.inject.Inject

/**
 * Created by sammy on 08/11/2017.
 */

class MainActivity : AppCompatActivity(), MainContract.View {
    @Inject
    lateinit var presenter: MainContract.Presenter

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
    // endregion

    // region Helper Methods
    private fun setupDi() {
        DaggerMainComponent.builder()
                .mainModule(MainModule(this))
                .build()
                .inject(this)
    }

    private fun setClickListeners() {
        btnEnablePermission.setOnClickListener { presenter.enablePermissionClicked() }
    }

    private fun changeScreenState(state: Int) {
        vpMain.displayedChild = vpMain.indexOfChild(vpMain.findViewById(state))
    }
    // endregion
}