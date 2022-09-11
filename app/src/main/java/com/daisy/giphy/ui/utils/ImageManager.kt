package com.daisy.giphy.ui.utils

import android.content.Context
import android.net.Uri
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.AbstractDraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import javax.inject.Inject

class ImageManager @Inject constructor(
    context: Context,
) {
    init {
        val previewsDiskConfig = DiskCacheConfig.newBuilder(context)
            .setMaxCacheSize(250L * ByteConstants.MB).build()

        val qualityDiskConfig = DiskCacheConfig.newBuilder(context)
            .setMaxCacheSize(250L * ByteConstants.MB).build()

        val config = ImagePipelineConfig.newBuilder(context)
            .setSmallImageDiskCacheConfig(previewsDiskConfig)
            .setMainDiskCacheConfig(qualityDiskConfig)
            .build()

        Fresco.initialize(context, config)
    }

    companion object {
        fun getImage(
            view: SimpleDraweeView,
            url: String?,
            previewUrl: String?,
        ): AbstractDraweeController<*, *>? {
            return Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(
                    ImageRequestBuilder
                        .newBuilderWithSource(Uri.parse(previewUrl))
                        .setCacheChoice(ImageRequest.CacheChoice.SMALL)
                        .build()
                )
                .setUri(url)
                .setAutoPlayAnimations(true)
                .setOldController(view.controller)
                .build()
        }
    }
}