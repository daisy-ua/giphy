package com.daisy.domain.models

data class GIFObject(
    val id: String,

    val title: String,

    val fixedHeightUrl: String?,

    val originalUrl: String?,

    val fixedStill: String?
)