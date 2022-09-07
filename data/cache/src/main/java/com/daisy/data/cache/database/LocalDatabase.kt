package com.daisy.data.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.daisy.data.cache.converters.DateTimeConverters
import com.daisy.data.cache.dao.GIFDao
import com.daisy.data.cache.entities.GIFObjectEntity

@Database(
    entities = [
        GIFObjectEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeConverters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun gifDao(): GIFDao
}