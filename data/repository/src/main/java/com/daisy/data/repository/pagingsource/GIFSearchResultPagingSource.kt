package com.daisy.data.repository.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.daisy.data.cache.database.LocalDatabase
import com.daisy.data.network.services.GIFService
import com.daisy.data.repository.mappers.toDomain
import com.daisy.domain.models.GIFObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GIFSearchResultPagingSource(
    private val localDatabase: LocalDatabase,
    private val apiService: GIFService,
    private val query: String,
) : PagingSource<Int, GIFObject>() {
    override fun getRefreshKey(state: PagingState<Int, GIFObject>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GIFObject> {
        val nextPageNumber = params.key ?: 0

        return try {
            withContext(Dispatchers.IO) {
                val offset = nextPageNumber * params.loadSize

                val response = apiService.getSearchResultsGIFs(query, offset).data.filter { gif ->
                    !localDatabase.excludedGifDao().isGIFExcluded(gif.id)
                }

                val nextKey = if (response.isEmpty()) null else nextPageNumber + 1

                val prevKey = if (nextPageNumber == 0) null else nextPageNumber - 1

                LoadResult.Page(
                    data = response.toDomain(),
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}