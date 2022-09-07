package com.daisy.giphy.ui.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.daisy.domain.models.GIFObject
import com.daisy.giphy.R
import kotlinx.coroutines.Job


@GlideModule
class GlideModule : AppGlideModule()

object ImageManager {
    fun getImage(
        view: ImageView,
        gif: GIFObject,
        cacheCallback: (Context, GifDrawable, String) -> Job,
    ) =
        GlideApp.with(view.context)
            .asGif()
            .load(gif.images.fixedHeightImage.url)
            .error(R.drawable.ic_launcher_background)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    resource?.let { res ->
                        cacheCallback(view.context, res, gif.id)
                    }
                    return false
                }

            })
            .into(view)
}