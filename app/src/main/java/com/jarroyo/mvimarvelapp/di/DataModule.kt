package com.jarroyo.mvimarvelapp.di

import android.app.Application
import android.content.Context
import com.jarroyo.mvimarvelapp.data.local.AppRoomDatabase
import com.jarroyo.mvimarvelapp.data.local.DiskDataSource
import com.jarroyo.mvimarvelapp.data.local.DiskDataSourceImpl
import com.jarroyo.mvimarvelapp.data.remote.ApiService
import com.jarroyo.mvimarvelapp.data.remote.NetworkDataSource
import com.jarroyo.mvimarvelapp.data.remote.NetworkDataSourceImpl
import com.jarroyo.mvimarvelapp.data.remote.NetworkSystem
import com.jarroyo.mvimarvelapp.data.remote.NetworkSystemImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
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

    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppRoomDatabase {
        return AppRoomDatabase.createInstance(context as Application)
    }

    @Provides
    fun provideDiskDataSource(roomDatabase: AppRoomDatabase): DiskDataSource {
        return DiskDataSourceImpl(roomDatabase)
    }
}
