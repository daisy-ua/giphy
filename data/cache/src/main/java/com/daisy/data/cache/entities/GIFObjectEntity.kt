package com.daisy.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daisy.data.cache.constants.Constants

@Entity(tableName = Constants.CACHE_TABLE)
data class GIFObjectEntity(
    @PrimaryKey(autoGenerate = true) val pk: Long = 0,

    val uid: String,

    val title: String,

    @ColumnInfo(name = "fixed_url")
    val fixedHeightUrl: String?,

    @ColumnInfo(name = "original_url")
    val originalUrl: String?,

    @ColumnInfo(name = "fixed_still")
    val fixedStill: String?,
)
