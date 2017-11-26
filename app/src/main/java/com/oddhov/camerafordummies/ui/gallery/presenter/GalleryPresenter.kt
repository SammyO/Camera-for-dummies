package com.oddhov.camerafordummies.ui.gallery.presenter

import com.oddhov.camerafordummies.ui.gallery.GalleryContract
import com.oddhov.camerafordummies.ui.gallery.view.GalleryAdapter
import javax.inject.Inject


/**
 * Created by sammy on 22/11/2017.
 */
class GalleryPresenter
@Inject
constructor(private val view: GalleryContract.View, private val repo: GalleryContract.Repo) : GalleryContract.Presenter {

    private lateinit var filePaths: ArrayList<String>

    override fun subscribe() {
        setupUi()
    }

    override fun unsubscribe() {

    }

    // region GalleryContract.Presenter Methods
    override fun onBindViewHolder(viewHolder: GalleryAdapter.ViewHolder, position: Int) {
        viewHolder.setThumbnail(filePaths[position])
    }

    override fun getItemCount(): Int {
        return filePaths.size
    }
    // endregion

    // region Helper Methods (UI)
    private fun setupUi() {
        filePaths = repo.getLocalImages()
        // The only reason we manage the adapter from here, is so that we can fetch the local images first
        view.setupAdapter()
    }
    // endregion
}