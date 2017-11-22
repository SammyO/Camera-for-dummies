package com.oddhov.camerafordummies.di.component

import com.oddhov.camerafordummies.di.modules.GalleryModule
import com.oddhov.camerafordummies.di.scopes.PerActivity
import com.oddhov.camerafordummies.ui.gallery.view.GalleryActivity
import dagger.Component

/**
 * Created by sammy on 09/11/2017.
 */
@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(GalleryModule::class))
interface GalleryComponent {
    fun inject(o: GalleryActivity)
}