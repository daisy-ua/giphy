package com.daisy.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.daisy.data.cache.entities.GIFObjectEntity

@Dao
interface GIFDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGIF(gif: GIFObjectEntity)
}