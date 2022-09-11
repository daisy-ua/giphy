package com.daisy.data.network.models

import com.google.gson.annotations.SerializedName

data class ImageObjectDto(
    val webp: String?,

    val url: String?,

    val height: String,

    val width: String,
)

data class ImagesDto(
    @SerializedName("fixed_height")
    val fixedHeight: ImageObjectDto,

    val original: ImageObjectDto,

    @SerializedName("fixed_height_still")
    val fixedStill: ImageObjectDto
)