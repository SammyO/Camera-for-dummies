package com.oddhov.camerafordummies.data.utils

import dagger.Module
import dagger.Provides

/**
 * Created by sammy on 17/11/2017.
 */

@Module
class UtilsModule {
    @Provides
    internal fun providePhotoUtils(photoUtils: PhotoUtils): PhotoUtils {
        return photoUtils
    }
}