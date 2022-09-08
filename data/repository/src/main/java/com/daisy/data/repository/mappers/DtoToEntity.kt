package com.daisy.data.repository.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.daisy.data.cache.entities.GIFObjectEntity
import com.daisy.data.network.models.GIFObjectDto
import java.time.LocalDateTime

fun List<GIFObjectDto>.toEntity() = map { it.toEntity() }

@RequiresApi(Build.VERSION_CODES.O)
fun GIFObjectDto.toEntity() =
    GIFObjectEntity(uid = id,
        title = title,
        url = images.fixedHeightImage.url,
        dateAdded = LocalDateTime.now())