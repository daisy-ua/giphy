package com.daisy.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.daisy.data.cache.constants.Constants
import com.daisy.data.cache.entities.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM ${Constants.REMOTE_KEYS_TABLE} WHERE gifId = :gifId")
    suspend fun remoteKeysGifId(gifId: String): RemoteKeys?

    @Query("DELETE FROM ${Constants.REMOTE_KEYS_TABLE}")
    suspend fun clearRemoteKeys()

}