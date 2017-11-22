package com.oddhov.camerafordummies.ui.gallery.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.oddhov.camerafordummies.CameraForDummiesApplication
import com.oddhov.camerafordummies.R
import com.oddhov.camerafordummies.di.component.DaggerGalleryComponent
import com.oddhov.camerafordummies.di.modules.GalleryModule
import com.oddhov.camerafordummies.ui.gallery.GalleryContract
import javax.inject.Inject

/**
 * Created by sammy on 22/11/2017.
 */
class GalleryActivity : AppCompatActivity(), GalleryContract.View {
    @Inject
    lateinit var presenter: GalleryContract.Presenter

    // region Activity Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupDi()
        setContentView(R.layout.activity_gallery)
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribe()
    }

    override fun onStop() {
        presenter.unsubscribe()
        super.onStop()
    }
    // endregion

    // region Helper Methods (UI)
    private fun setupDi() {
        DaggerGalleryComponent.builder()
                .applicationComponent((application as CameraForDummiesApplication).getComponent())
                .galleryModule(GalleryModule(this))
                .build()
                .inject(this)
    }
    // endregion

}