package com.daisy.data.repository.mappers

import com.daisy.data.cache.entities.GIFObjectEntity
import com.daisy.domain.models.GIFObject

fun GIFObjectEntity.toDomain() = GIFObject(uid, title, url)