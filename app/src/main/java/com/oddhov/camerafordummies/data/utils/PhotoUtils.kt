package com.oddhov.camerafordummies.data.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import com.oddhov.camerafordummies.di.scopes.ApplicationContext
import io.reactivex.Completable
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

/**
 * Created by sammy on 17/11/2017.
 */

class PhotoUtils
@Inject
constructor(@ApplicationContext private val context: Context) {
     fun storeBitmap(bitmap: Bitmap): Completable {
         return Completable.create {
             val file = createFile()
             if (file == null) {
                 it.onError(Throwable("File is null"))
             }

             try {
                 val out = FileOutputStream(file)
                 bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                 out.flush()
                 out.close()
                 MediaScannerConnection.scanFile(context, arrayOf(file.toString()), null
                 ) { _, _ -> }
                 it.onComplete()
             } catch (e: Exception) {
                 it.onError(e)
             }
         }
    }

    /**
     * Creates a file in external storage and returns the file path
     */
    @SuppressLint("SimpleDateFormat")
    private fun createFile(): File? {
        val currentTimeMillis = System.currentTimeMillis()
        val today = Date(currentTimeMillis)
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
        val title = dateFormat.format(today)
        val fileName = "IMG_$title.png"

        val extStorageDir = Environment.getExternalStorageDirectory()
        if (extStorageDir.canWrite()) {
            val imageDir = File(extStorageDir.path + "/camerafordummies")
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }
            if (imageDir.canWrite()) {
                val file = File(imageDir, fileName)
                if (file.exists())
                    file.delete()
                return file
            }
        }
        return null
    }
}