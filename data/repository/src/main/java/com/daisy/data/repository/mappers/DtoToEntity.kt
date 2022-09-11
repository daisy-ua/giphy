package com.daisy.data.repository.mappers

import com.daisy.data.cache.entities.GIFObjectEntity
import com.daisy.data.network.models.GIFObjectDto

internal fun List<GIFObjectDto>.toEntity() = map { it.toEntity() }

internal fun GIFObjectDto.toEntity() =
    GIFObjectEntity(
        uid = id,
        title = title,
        fixedHeightUrl = images.fixedHeight.let {
            it.webp ?: it.url
        },
        originalUrl = images.original.let {
            it.webp ?: it.url
        },
        fixedStill = images.fixedStill.url
    )