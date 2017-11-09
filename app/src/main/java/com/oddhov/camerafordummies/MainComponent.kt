package com.oddhov.camerafordummies

import com.oddhov.camerafordummies.data.di.PerActivity
import com.oddhov.camerafordummies.ui.main.view.MainActivity
import dagger.Component

/**
 * Created by sammy on 09/11/2017.
 */

@PerActivity
@Component(modules = arrayOf(MainModule::class))
interface MainComponent {
    fun inject(o: MainActivity)
}