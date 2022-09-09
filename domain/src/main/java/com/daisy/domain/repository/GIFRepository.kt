package com.daisy.domain.repository

import androidx.paging.PagingData
import com.daisy.domain.models.GIFObject
import kotlinx.coroutines.flow.Flow

interface GIFRepository {

    suspend fun fetchTrendingGIFs(): Flow<PagingData<GIFObject>>

    suspend fun fetchSearchResultGIFs(query: String): Flow<PagingData<GIFObject>>

    suspend fun excludeGIFsFromResponse(ids: List<String>)
}