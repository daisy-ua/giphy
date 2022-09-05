package com.daisy.data.repository.repository

import androidx.paging.PagingData
import com.daisy.data.network.models.GIFObjectDto
import com.daisy.data.network.models.GIFObjectListDto
import kotlinx.coroutines.flow.Flow

interface GIFRepository {

    suspend fun fetchTrendingGIFs(): Flow<PagingData<GIFObjectDto>>

    suspend fun fetchSearchResultGIFs(
        query: String,
    ): Flow<PagingData<GIFObjectDto>>

    suspend fun test() : GIFObjectListDto?
}