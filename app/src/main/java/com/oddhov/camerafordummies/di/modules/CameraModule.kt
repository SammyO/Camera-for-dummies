package com.oddhov.camerafordummies.di.modules

import com.oddhov.camerafordummies.di.scopes.PerActivity
import com.oddhov.camerafordummies.ui.camera.CameraContract
import com.oddhov.camerafordummies.ui.camera.model.CameraRepo
import com.oddhov.camerafordummies.ui.camera.presenter.CameraPresenter
import com.oddhov.camerafordummies.ui.camera.view.CameraActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides

/**
 * Created by sammy on 09/11/2017.
 */
@PerActivity
@Module
class CameraModule(private val activity: CameraActivity) {

    @Provides
    internal fun provideView(): CameraContract.View {
        return activity
    }

    @Provides
    internal fun providePresenter(presenter: CameraPresenter, repo: CameraContract.Repo,
                                  rxPermissions: RxPermissions): CameraContract.Presenter {
        return presenter
    }

    @Provides
    internal fun provideRepo(repo: CameraRepo): CameraContract.Repo {
        return repo
    }

    @Provides
    internal fun providesRxPermissions(): RxPermissions {
        return RxPermissions(activity)
    }
}