package com.daisy.data.repository.repository

import androidx.paging.*
import com.daisy.data.cache.database.LocalDatabase
import com.daisy.data.network.services.GIFService
import com.daisy.data.repository.mappers.toDomain
import com.daisy.data.repository.mediator.GIFRemoteMediator
import com.daisy.data.repository.pagingsource.GIFSearchResultPagingSource
import com.daisy.domain.models.GIFObject
//import com.daisy.domain.models.ImageObject
//import com.daisy.domain.models.Images
import com.daisy.domain.repository.GIFRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GIFRepositoryImpl @Inject constructor(
    private val remoteDataSource: GIFService,
    private val localDatabase: LocalDatabase,
) : GIFRepository {
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun fetchTrendingGIFs(): Flow<PagingData<GIFObject>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = GIFRemoteMediator(
                remoteDataSource,
                localDatabase
            ),
            pagingSourceFactory = { localDatabase.gifDao().getGIFs() }
        ).flow
            .map { pagingData ->
                pagingData.map { entity ->
                    entity.toDomain()
                }
            }
    }

    override suspend fun fetchSearchResultGIFs(query: String): Flow<PagingData<GIFObject>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GIFSearchResultPagingSource(remoteDataSource, query) }
        ).flow
    }
}