package com.daisy.data.cache.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daisy.data.cache.constants.Constants

@Entity(tableName = Constants.EXCLUDED_GIFS)
data class ExcludedGIF(
    @PrimaryKey val uid: String,
)
