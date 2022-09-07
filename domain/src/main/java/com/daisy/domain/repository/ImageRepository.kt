package com.daisy.domain.repository

import android.content.Context
import com.bumptech.glide.load.resource.gif.GifDrawable

interface ImageRepository {

    suspend fun cacheImage(
        context: Context,
        gifDrawable: GifDrawable,
        filename: String,
    ): String?
}