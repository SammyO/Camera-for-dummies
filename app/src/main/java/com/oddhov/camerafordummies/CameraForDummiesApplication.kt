package com.oddhov.camerafordummies

import android.app.Application
import com.oddhov.camerafordummies.data.utils.UtilsModule
import com.oddhov.camerafordummies.di.component.ApplicationComponent
import com.oddhov.camerafordummies.di.component.DaggerApplicationComponent
import com.oddhov.camerafordummies.di.modules.ApplicationModule
import timber.log.Timber

/**
 * Created by sammy on 19/11/2017.
 */

class CameraForDummiesApplication : Application() {
    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .utilsModule(UtilsModule())
                .build()

        applicationComponent.inject(this)
    }

    fun getComponent(): ApplicationComponent {
        return applicationComponent
    }


    // Needed to replace the component with a test specific one
    fun setComponent(applicationComponent: ApplicationComponent) {
        this.applicationComponent = applicationComponent
    }
}