package com.oddhov.camerafordummies.di.component

import com.oddhov.camerafordummies.di.modules.CameraModule
import com.oddhov.camerafordummies.di.scopes.PerActivity
import com.oddhov.camerafordummies.ui.camera.view.CameraActivity
import dagger.Component

/**
 * Created by sammy on 09/11/2017.
 */
@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(CameraModule::class))
interface CameraComponent {
    fun inject(o: CameraActivity)
}