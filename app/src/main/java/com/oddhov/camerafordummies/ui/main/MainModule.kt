package com.oddhov.camerafordummies.ui.main

import com.oddhov.camerafordummies.data.di.PerActivity
import com.oddhov.camerafordummies.ui.main.model.MainRepo
import com.oddhov.camerafordummies.ui.main.presenter.MainPresenter
import com.oddhov.camerafordummies.ui.main.view.MainActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides

/**
 * Created by sammy on 09/11/2017.
 */

@PerActivity
@Module
class MainModule(private val activity: MainActivity) {

    @Provides
    internal fun provideView(): MainContract.View {
        return activity
    }

    @Provides
    internal fun providePresenter(repo: MainContract.Repo, rxPermissions: RxPermissions): MainContract.Presenter {
        return MainPresenter(activity, repo, rxPermissions)
    }

    @Provides
    internal fun provideRepo(): MainContract.Repo {
        return MainRepo()
    }

    @Provides
    internal fun providesRxPermissions(): RxPermissions {
        return RxPermissions(activity)
    }
}