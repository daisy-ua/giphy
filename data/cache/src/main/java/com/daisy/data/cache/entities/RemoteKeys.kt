package com.daisy.data.cache.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daisy.data.cache.constants.Constants

@Entity(tableName = Constants.REMOTE_KEYS_TABLE)
data class RemoteKeys(
    @PrimaryKey
    val gifId: String,

    val prevKey: Int?,

    val nextKey: Int?
)
