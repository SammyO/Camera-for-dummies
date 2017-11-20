package com.oddhov.camerafordummies.di.component

import android.content.Context
import com.oddhov.camerafordummies.CameraForDummiesApplication
import com.oddhov.camerafordummies.data.utils.UtilsModule
import com.oddhov.camerafordummies.di.modules.ApplicationModule
import com.oddhov.camerafordummies.di.scopes.ApplicationContext
import dagger.Component
import javax.inject.Singleton

/**
 * Created by sammy on 19/11/2017.
 */

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, UtilsModule::class))
interface ApplicationComponent {

    fun inject(app: CameraForDummiesApplication)

    @ApplicationContext
    fun context(): Context
}