package com.daisy.data.repository.mappers

import com.daisy.data.network.models.GIFObjectDto
import com.daisy.domain.models.GIFObject

internal fun List<GIFObjectDto>.toDomain() = map { gifObjectDto -> gifObjectDto.toDomain() }

internal fun GIFObjectDto.toDomain() =
    GIFObject(
        id = id,
        title = title,
        fixedHeightUrl = images.fixedHeight.let {
            it.webp ?: it.url
        },
        originalUrl = images.original.let {
            it.webp ?: it.url
        },
        fixedStill = images.fixedStill.url
    )