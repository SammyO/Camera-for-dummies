package com.oddhov.camerafordummies.di.modules

import android.app.Application
import android.content.Context
import com.oddhov.camerafordummies.di.scopes.ApplicationContext
import dagger.Module
import dagger.Provides

/**
 * Created by sammy on 19/11/2017.
 */

@Module
class ApplicationModule(private val mApplication: Application) {

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context {
        return mApplication
    }
}
