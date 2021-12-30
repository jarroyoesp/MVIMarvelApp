package com.jarroyo.mvimarvelapp.data.repository

import com.jarroyo.mvimarvelapp.data.remote.NetworkDataSource
import com.jarroyo.mvimarvelapp.data.remote.NetworkDataSourceImpl
import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DataRepositoryImpl(
    private val networkDataSource: NetworkDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : DataRepository {

    companion object {
        private val TAG = DataRepositoryImpl::class.java.simpleName
    }

    override suspend fun getList(page: Int): Result<List<UiModel>?> {
        return withContext(ioDispatcher){
            networkDataSource.getCharacterList(page)
        }
    }
}