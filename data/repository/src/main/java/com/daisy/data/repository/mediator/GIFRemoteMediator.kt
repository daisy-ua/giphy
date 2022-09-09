package com.daisy.data.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.daisy.data.cache.database.LocalDatabase
import com.daisy.data.cache.entities.GIFObjectEntity
import com.daisy.data.cache.entities.RemoteKeys
import com.daisy.data.network.services.GIFService
import com.daisy.data.repository.mappers.toEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class GIFRemoteMediator(
    private val remoteSource: GIFService,
    private val localDatabase: LocalDatabase,
) : RemoteMediator<Int, GIFObjectEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GIFObjectEntity>,
    ): MediatorResult {
        val nextPageNumber = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 0
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        val offset = nextPageNumber * state.config.pageSize

        try {
            val apiResponse = remoteSource.getTrendingGIFs(offset)
            val gifs = apiResponse.data.filter { gif ->
                !localDatabase.excludedGifDao().isGIFExcluded(gif.id)
            }
            val endOfPaginationReached = gifs.isEmpty()

            localDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localDatabase.remoteKeysDao().clearRemoteKeys()
                    localDatabase.gifDao().clearCache()
                }

                val nextKey = if (endOfPaginationReached) null else nextPageNumber + 1

                val prevKey = if (nextPageNumber == 0) null else nextPageNumber - 1

                val keys = gifs.map {
                    RemoteKeys(gifId = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                localDatabase.remoteKeysDao().insertAll(keys)
                localDatabase.gifDao().insertAll(gifs.toEntity())
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, GIFObjectEntity>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { gif ->
                localDatabase.remoteKeysDao().remoteKeysGifId(gif.uid)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, GIFObjectEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { gif ->
                localDatabase.remoteKeysDao().remoteKeysGifId(gif.uid)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, GIFObjectEntity>,
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.uid?.let { gifId ->
                localDatabase.remoteKeysDao().remoteKeysGifId(gifId)
            }
        }
    }
}