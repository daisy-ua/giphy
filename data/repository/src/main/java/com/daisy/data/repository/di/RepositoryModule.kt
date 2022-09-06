package com.daisy.data.repository.di

import com.daisy.domain.repository.GIFRepository
import com.daisy.data.repository.repository.GIFRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindGIFRepository(
        impl: GIFRepositoryImpl,
    ): GIFRepository
}