package com.daisy.data.repository.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.daisy.data.network.models.GIFObjectDto
import com.daisy.data.network.services.GIFService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GIFTrendingPagingSource(
    private val apiService: GIFService,
) : PagingSource<Int, GIFObjectDto>() {

    override fun getRefreshKey(state: PagingState<Int, GIFObjectDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GIFObjectDto> {
        val nextPageNumber = params.key ?: 0

        return try {
            withContext(Dispatchers.IO) {
                val offset = nextPageNumber * params.loadSize

                val response = apiService.getTrendingGIFs(offset)
                Log.d("daisy", response.data.toString())
                val nextKey = if (response.data.isEmpty()) null else nextPageNumber + 1

                val prevKey = if (nextPageNumber == 0) null else nextPageNumber - 1

                LoadResult.Page(
                    data = response.data,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}