package com.oddhov.camerafordummies.di.component

import com.oddhov.camerafordummies.di.modules.MainModule
import com.oddhov.camerafordummies.di.scopes.PerActivity
import com.oddhov.camerafordummies.ui.main.view.MainActivity
import dagger.Component

/**
 * Created by sammy on 09/11/2017.
 */

@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(MainModule::class))
interface ActivityComponent {
    fun inject(o: MainActivity)
}