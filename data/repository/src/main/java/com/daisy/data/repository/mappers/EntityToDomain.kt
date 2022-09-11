package com.daisy.data.repository.mappers

import com.daisy.data.cache.entities.GIFObjectEntity
import com.daisy.domain.models.GIFObject

internal fun GIFObjectEntity.toDomain() =
    GIFObject(uid, title, fixedHeightUrl, originalUrl, fixedStill)