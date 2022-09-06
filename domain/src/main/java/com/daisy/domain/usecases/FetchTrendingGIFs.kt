package com.daisy.domain.usecases

import com.daisy.domain.repository.GIFRepository
import javax.inject.Inject

class FetchTrendingGIFs @Inject constructor(
    private val repository: GIFRepository,
) {
    suspend operator fun invoke() = repository.fetchTrendingGIFs()
}