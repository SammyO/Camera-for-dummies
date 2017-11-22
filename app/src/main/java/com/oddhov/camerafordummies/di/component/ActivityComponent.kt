package com.oddhov.camerafordummies.di.component

import com.oddhov.camerafordummies.di.modules.CameraModule
import com.oddhov.camerafordummies.di.modules.GalleryModule
import com.oddhov.camerafordummies.di.scopes.PerActivity
import com.oddhov.camerafordummies.ui.camera.view.CameraActivity
import com.oddhov.camerafordummies.ui.gallery.view.GalleryActivity
import dagger.Component

/**
 * Created by sammy on 09/11/2017.
 */
@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(CameraModule::class, GalleryModule::class))
interface ActivityComponent {
    fun inject(o: CameraActivity)

    fun inject(o: GalleryActivity)
}