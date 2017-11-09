package com.oddhov.camerafordummies

import com.oddhov.camerafordummies.data.di.PerActivity
import com.oddhov.camerafordummies.ui.main.model.MainRepo
import com.oddhov.camerafordummies.ui.main.presenter.MainPresenter
import com.oddhov.camerafordummies.ui.main.view.MainActivity
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
    internal fun providePresenter(view: MainContract.View, repo: MainContract.Repo): MainContract.Presenter {
        return MainPresenter(view, repo)
    }

    @Provides
    internal fun provideRepo(): MainContract.Repo {
        return MainRepo()
    }
}