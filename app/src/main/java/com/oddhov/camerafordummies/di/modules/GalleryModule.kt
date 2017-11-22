package com.oddhov.camerafordummies.di.modules

import com.oddhov.camerafordummies.di.scopes.PerActivity
import com.oddhov.camerafordummies.ui.gallery.GalleryContract
import com.oddhov.camerafordummies.ui.gallery.model.GalleryRepo
import com.oddhov.camerafordummies.ui.gallery.presenter.GalleryPresenter
import com.oddhov.camerafordummies.ui.gallery.view.GalleryActivity
import dagger.Module
import dagger.Provides

/**
 * Created by sammy on 09/11/2017.
 */
@PerActivity
@Module
class GalleryModule(private val activity: GalleryActivity) {

    @Provides
    internal fun provideView(): GalleryContract.View {
        return activity
    }

    @Provides
    internal fun providePresenter(presenter: GalleryPresenter, repo: GalleryContract.Repo): GalleryContract.Presenter {
        return presenter
    }

    @Provides
    internal fun provideRepo(repo: GalleryRepo): GalleryContract.Repo {
        return repo
    }
}