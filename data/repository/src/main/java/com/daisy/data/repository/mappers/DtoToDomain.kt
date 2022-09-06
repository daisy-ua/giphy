package com.daisy.data.repository.mappers

import com.daisy.data.network.models.GIFObjectDto
import com.daisy.data.network.models.GIFObjectListDto
import com.daisy.data.network.models.ImageObjectDto
import com.daisy.data.network.models.ImagesDto
import com.daisy.domain.models.GIFObject
import com.daisy.domain.models.ImageObject
import com.daisy.domain.models.Images

fun GIFObjectListDto.toDomain() = data.map { gifObjectDto -> gifObjectDto.toDomain() }

fun GIFObjectDto.toDomain() = GIFObject(id, title, images.toDomain())

private fun ImagesDto.toDomain() = Images(fixedHeightImage.toDomain())

private fun ImageObjectDto.toDomain() = ImageObject(url)