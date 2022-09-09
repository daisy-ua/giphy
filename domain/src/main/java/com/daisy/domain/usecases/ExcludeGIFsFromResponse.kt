package com.daisy.domain.usecases

import com.daisy.domain.repository.GIFRepository
import javax.inject.Inject

class ExcludeGIFsFromResponse @Inject constructor(
    private val repository: GIFRepository,
) {
    suspend operator fun invoke(ids: List<String>) = repository.excludeGIFsFromResponse(ids)
}