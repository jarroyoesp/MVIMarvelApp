package com.jarroyo.mvimarvelapp.di

import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.testing.TestInstallIn
import java.com.jarroyo.marvel.data.repository.FakeDataRepositoryImpl

@TestInstallIn(
    components = [ActivityRetainedComponent::class],
    replaces = [RepositoryModule::class]
)
@Module
class FakeRepositoryModule {

    @Provides
    fun getProfileSource(): DataRepository = FakeDataRepositoryImpl()
}
