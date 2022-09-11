package com.daisy.data.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.daisy.data.cache.dao.ExcludedGIFDao
import com.daisy.data.cache.dao.GIFDao
import com.daisy.data.cache.dao.RemoteKeysDao
import com.daisy.data.cache.entities.ExcludedGIF
import com.daisy.data.cache.entities.GIFObjectEntity
import com.daisy.data.cache.entities.RemoteKeys

@Database(
    entities = [
        GIFObjectEntity::class,
        RemoteKeys::class,
        ExcludedGIF::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun gifDao(): GIFDao

    abstract fun excludedGifDao(): ExcludedGIFDao

    abstract fun remoteKeysDao(): RemoteKeysDao
}