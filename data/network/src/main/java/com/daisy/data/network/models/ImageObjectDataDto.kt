package com.daisy.data.network.models

import com.google.gson.annotations.SerializedName

data class ImageObjectDto(
    val url: String,

    val height: String,

    val width: String,
)

data class ImagesDto(
    @SerializedName("fixed_height")
    val fixedHeightImage: ImageObjectDto,
)