package com.daisy.data.repository.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.daisy.data.network.models.GIFObjectDto
import com.daisy.data.network.models.GIFObjectListDto
import com.daisy.data.network.services.GIFService
import com.daisy.data.repository.pagingsource.GIFTrendingPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GIFRepositoryImpl @Inject constructor(
    private val remoteDataSource: GIFService,
) : GIFRepository {
    override suspend fun fetchTrendingGIFs(): Flow<PagingData<GIFObjectDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GIFTrendingPagingSource(remoteDataSource) }
        ).flow
    }

    override suspend fun fetchSearchResultGIFs(query: String): Flow<PagingData<GIFObjectDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun test(): GIFObjectListDto? {
        return try {
            remoteDataSource.getTrendingGIFs(5)
        } catch (ex: Exception) {
            null
        }
    }
}