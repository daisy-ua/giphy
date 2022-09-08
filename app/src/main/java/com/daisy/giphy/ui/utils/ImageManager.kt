package com.daisy.giphy.ui.utils

import android.widget.ImageView
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.module.AppGlideModule
import com.daisy.domain.models.GIFObject
import com.daisy.giphy.R


@GlideModule
class GlideModule : AppGlideModule()

object ImageManager {
    fun getImage(
        view: ImageView,
        gif: GIFObject,
    ) =
        GlideApp.with(view.context)
            .asGif()
            .load(gif.url)
            .error(R.drawable.ic_launcher_background)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(view)
}