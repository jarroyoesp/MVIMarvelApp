package com.jarroyo.mvimarvelapp.di

import android.content.Context
import com.jarroyo.mvimarvelapp.data.remote.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ActivityRetainedComponent::class)
@Module
object DataModule {

    @Provides
    fun provideNetworkSystem(@ApplicationContext context: Context) =
        NetworkSystemImpl(context) as NetworkSystem

    @Provides
    fun provideRemoteNetworkDataSource(
        apiService: ApiService,
        networkSystem: NetworkSystem
    ): NetworkDataSource {
        return NetworkDataSourceImpl(apiService, networkSystem)
    }
/*
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.createInstance(context as Application)
    }

    @Provides
    fun provideDiskDataSource(database: AppDatabase): DiskDataSource {
        return DiskDataSourceImpl(database)
    }*/

}