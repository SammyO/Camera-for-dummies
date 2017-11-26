package com.oddhov.camerafordummies.ui.gallery.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.oddhov.camerafordummies.R
import com.oddhov.camerafordummies.ui.gallery.GalleryContract
import kotlinx.android.synthetic.main.item_gallery_thumbnail.view.ivThumbnail


/**
 * Created by sammy on 24/11/2017.
 */
class GalleryAdapter(private val presenter: GalleryContract.Presenter) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.item_gallery_thumbnail,
                parent,
                false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        presenter.onBindViewHolder(holder, position)
    }

    override fun getItemCount(): Int {
        return presenter.getItemCount()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view), GalleryContract.GalleryViewHolder {

        override fun setThumbnail(path: String) {
            Glide.with(view)
                    .load(path)
                    .into(view.ivThumbnail)
        }
    }

}