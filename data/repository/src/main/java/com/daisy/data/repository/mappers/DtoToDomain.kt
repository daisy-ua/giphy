package com.daisy.data.repository.mappers

import com.daisy.data.network.models.GIFObjectDto
import com.daisy.data.network.models.GIFObjectListDto
import com.daisy.domain.models.GIFObject

fun GIFObjectListDto.toDomain() = data.map { gifObjectDto -> gifObjectDto.toDomain() }

fun GIFObjectDto.toDomain() = GIFObject(id, title, images.fixedHeightImage.url)