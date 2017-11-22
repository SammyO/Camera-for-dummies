package com.oddhov.camerafordummies.ui.gallery.presenter

import com.oddhov.camerafordummies.ui.gallery.GalleryContract
import javax.inject.Inject

/**
 * Created by sammy on 22/11/2017.
 */

class GalleryPresenter
@Inject
constructor(private val view: GalleryContract.View, private val repo: GalleryContract.Repo) : GalleryContract.Presenter {

    override fun subscribe() {

    }

    override fun unsubscribe() {

    }
}