package com.jarroyo.mvimarvelapp.data.repository

import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import kotlinx.coroutines.CoroutineDispatcher

class DataRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher
) : DataRepository {

    companion object {
        private val TAG = DataRepositoryImpl::class.java.simpleName
    }

    override suspend fun getList(page: Int): Result<List<UiModel>?> {
        return Result.success(listOf(UiModel(1, "Name", "Description", "imageUrl")))
    }
}