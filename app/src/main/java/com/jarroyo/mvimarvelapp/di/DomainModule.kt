package com.jarroyo.mvimarvelapp.di

import com.jarroyo.mvimarvelapp.domain.interactors.GetListInteractor
import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object DomainModule {

    @Provides
    fun provideGetListInteractor(repository: DataRepository): GetListInteractor {
        return GetListInteractor(repository)
    }
}