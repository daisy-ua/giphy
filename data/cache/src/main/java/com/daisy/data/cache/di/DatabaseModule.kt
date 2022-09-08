package com.daisy.data.cache.di

import android.content.Context
import androidx.room.Room
import com.daisy.data.cache.constants.Constants
import com.daisy.data.cache.dao.GIFDao
import com.daisy.data.cache.database.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): LocalDatabase {
        return Room.databaseBuilder(
            appContext,
            LocalDatabase::class.java,
            Constants.DATABASE
        ).build()
    }

    @Provides
    @Singleton
    fun provideGIFDao(database: LocalDatabase): GIFDao = database.gifDao()
}