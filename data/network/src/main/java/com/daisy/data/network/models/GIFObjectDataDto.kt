package com.daisy.data.network.models

data class GIFObjectDto(
    val id: String,

    val title: String,

    val images: ImagesDto,
)

data class GIFObjectListDto(
    val data: List<GIFObjectDto>,
)