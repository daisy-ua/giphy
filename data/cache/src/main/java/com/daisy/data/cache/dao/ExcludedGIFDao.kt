package com.daisy.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.daisy.data.cache.constants.Constants
import com.daisy.data.cache.entities.ExcludedGIF

@Dao
interface ExcludedGIFDao {
    @Query("SELECT EXISTS(SELECT * FROM ${Constants.EXCLUDED_GIFS} WHERE uid = :id)")
    suspend fun isGIFExcluded(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(gifs: List<ExcludedGIF>)
}