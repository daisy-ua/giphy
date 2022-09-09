package com.daisy.data.cache.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.daisy.data.cache.constants.Constants
import com.daisy.data.cache.entities.GIFObjectEntity

@Dao
interface GIFDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<GIFObjectEntity>)

    @Query("DELETE FROM ${Constants.CACHE_TABLE}")
    suspend fun clearCache()

    @Query("DELETE FROM ${Constants.CACHE_TABLE} where uid in (:ids)")
    suspend fun deleteGIFs(ids: List<String>)

    @Query("SELECT * FROM ${Constants.CACHE_TABLE} ORDER BY date_added ASC")
    fun getGIFs(): PagingSource<Int, GIFObjectEntity>
}