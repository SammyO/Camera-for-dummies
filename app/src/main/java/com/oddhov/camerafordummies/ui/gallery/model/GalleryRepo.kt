package com.oddhov.camerafordummies.ui.gallery.model

import com.oddhov.camerafordummies.ui.gallery.GalleryContract
import java.io.File
import javax.inject.Inject


/**
 * Created by sammy on 09/11/2017.
 */
class GalleryRepo
@Inject
constructor() : GalleryContract.Repo {
    override fun getLocalImages(): ArrayList<String> {
        val filePaths = ArrayList<String>()
        val file = File(android.os.Environment.getExternalStorageDirectory(), "/camerafordummies")
        if (file.isDirectory) {
            val files = file.listFiles()
            (0 until files.size).mapTo(filePaths) { files[it].absolutePath }
        }
        return filePaths
    }
}