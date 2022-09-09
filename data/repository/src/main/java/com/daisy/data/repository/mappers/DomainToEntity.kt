package com.daisy.data.repository.mappers

import com.daisy.data.cache.entities.ExcludedGIF

internal fun getExcludedEntities(ids: List<String>) = ids.map { ExcludedGIF(it) }