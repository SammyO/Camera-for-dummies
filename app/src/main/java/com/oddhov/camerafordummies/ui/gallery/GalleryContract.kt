package com.oddhov.camerafordummies.ui.gallery

import com.oddhov.camerafordummies.ui.base.BasePresenter
import com.oddhov.camerafordummies.ui.gallery.view.GalleryAdapter

/**
 * Created by sammy on 22/11/2017.
 */
interface GalleryContract {
    interface View {
        fun setupAdapter()
    }

    interface GalleryViewHolder {
        fun setThumbnail(path: String)
    }

    interface Presenter : BasePresenter {

        // Adapter-invoked functions
        fun onBindViewHolder(viewHolder: GalleryAdapter.ViewHolder, position: Int)
        fun getItemCount(): Int
    }

    interface Repo {
        fun getLocalImages(): ArrayList<String>
    }
}