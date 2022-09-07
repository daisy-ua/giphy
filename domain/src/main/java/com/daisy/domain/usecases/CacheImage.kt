package com.daisy.domain.usecases

import android.content.Context
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.daisy.domain.repository.ImageRepository
import javax.inject.Inject

class CacheImage @Inject constructor(
    private val repository: ImageRepository,
) {
    suspend operator fun invoke(
        context: Context,
        gifDrawable: GifDrawable,
        filename: String,
    ) = repository.cacheImage(context, gifDrawable, filename)
}