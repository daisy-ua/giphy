package com.daisy.data.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daisy.data.cache.Constants
import java.time.LocalDateTime

@Entity(tableName = Constants.CACHE_TABLE)
data class GIFObjectEntity(
    @PrimaryKey val uid: String,

    val title: String,

    @ColumnInfo(name = "local_url")
    val url: String,

    @ColumnInfo(name = "date_added")
    val dateAdded: LocalDateTime
)
