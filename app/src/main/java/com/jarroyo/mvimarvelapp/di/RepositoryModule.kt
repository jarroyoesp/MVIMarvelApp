package com.jarroyo.mvimarvelapp.di

import com.jarroyo.mvimarvelapp.data.remote.NetworkDataSource
import com.jarroyo.mvimarvelapp.data.repository.DataRepositoryImpl
import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(ViewModelComponent::class)
@Module
object RepositoryModule {

    @Provides
    fun provideDataRepository(
        networkDataSource: NetworkDataSource,
        ioDispatcher: CoroutineDispatcher
    ): DataRepository {
        return DataRepositoryImpl(networkDataSource, ioDispatcher)
    }
}