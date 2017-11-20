package com.oddhov.camerafordummies.di.modules

import com.oddhov.camerafordummies.di.scopes.PerActivity
import com.oddhov.camerafordummies.ui.main.MainContract
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
    internal fun providePresenter(presenter: MainPresenter, repo: MainContract.Repo,
                                  rxPermissions: RxPermissions): MainContract.Presenter {
        return presenter
    }

    @Provides
    internal fun provideRepo(repo: MainRepo): MainContract.Repo {
        return repo
    }

    @Provides
    internal fun providesRxPermissions(): RxPermissions {
        return RxPermissions(activity)
    }
}