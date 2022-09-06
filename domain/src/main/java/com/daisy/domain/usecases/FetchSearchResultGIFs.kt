package com.daisy.domain.usecases

import com.daisy.domain.repository.GIFRepository
import javax.inject.Inject

class FetchSearchResultGIFs  @Inject constructor(
    private val repository: GIFRepository,
) {
    suspend operator fun invoke(query: String) = repository.fetchSearchResultGIFs(query)
}