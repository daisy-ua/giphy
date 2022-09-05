package com.daisy.giphy.ui.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.daisy.giphy.R

object ImageManager {
    fun getImage(view: ImageView, url: String?) =
        Glide.with(view.context)
            .asGif()
            .load(url)
            .error(R.drawable.ic_launcher_background)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(view)
}