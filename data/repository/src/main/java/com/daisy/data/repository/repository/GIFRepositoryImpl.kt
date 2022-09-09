package com.daisy.data.repository.repository

import androidx.paging.*
import androidx.room.withTransaction
import com.daisy.data.cache.database.LocalDatabase
import com.daisy.data.network.services.GIFService
import com.daisy.data.repository.mappers.getExcludedEntities
import com.daisy.data.repository.mappers.toDomain
import com.daisy.data.repository.mediator.GIFRemoteMediator
import com.daisy.data.repository.pagingsource.GIFSearchResultPagingSource
import com.daisy.domain.models.GIFObject
import com.daisy.domain.repository.GIFRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GIFRepositoryImpl @Inject constructor(
    private val remoteDataSource: GIFService,
    private val localDatabase: LocalDatabase,
) : GIFRepository {
    override suspend fun excludeGIFsFromResponse(ids: List<String>) {
        withContext(Dispatchers.IO) {
            localDatabase.excludedGifDao().insertAll(getExcludedEntities(ids))
            localDatabase.gifDao().deleteGIFs(ids)
            localDatabase.remoteKeysDao().deleteRemoteKeys(ids)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun fetchTrendingGIFs(): Flow<PagingData<GIFObject>> {
        return Pager(
            config = PagingConfig(
                pageSize = LOAD_PAGE_SIZE,
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
                pageSize = LOAD_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GIFSearchResultPagingSource(localDatabase,
                    remoteDataSource,
                    query)
            }
        ).flow
    }

    companion object {
        const val LOAD_PAGE_SIZE = 10
    }
}