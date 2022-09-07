package com.daisy.data.repository.di

import com.daisy.data.cache.dao.GIFDao
import com.daisy.data.network.services.GIFService
import com.daisy.data.repository.repository.GIFRepositoryImpl
import com.daisy.data.repository.repository.ImageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryImpModule {

    @Provides
    @Singleton
    fun provideGIFRepository(
        remoteSource: GIFService,
    ): GIFRepositoryImpl = GIFRepositoryImpl(remoteSource)

    @Provides
    @Singleton
    fun provideImageRepository(
        localSource: GIFDao
    ): ImageRepositoryImpl = ImageRepositoryImpl(localSource)
}