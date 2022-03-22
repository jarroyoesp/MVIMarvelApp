package com.jarroyo.mvimarvelapp.data.repository

import com.jarroyo.mvimarvelapp.data.local.DiskDataSource
import com.jarroyo.mvimarvelapp.data.remote.NetworkDataSource
import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.domain.model.toEntity
import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber

class DataRepositoryImpl(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : DataRepository {

    override suspend fun getList(page: Int): Result<List<UiModel>?> {
        Timber.d("Page: $page")
        return withContext(ioDispatcher) {
            networkDataSource.getCharacterList(page)
        }
    }

    override fun search(name: String): Flow<Result<List<UiModel>?>> = flow {
        Timber.d("Name: $name")
        emit(networkDataSource.searchCharacterList(name))
    }.flowOn(ioDispatcher)

    override suspend fun saveFavorite(uiModel: UiModel): Result<Boolean> {
        Timber.d("UiModel: $uiModel")
        return withContext(ioDispatcher) {
            diskDataSource.insertCharacter(uiModel.toEntity())
            Result.success(true)
        }
    }

    override suspend fun removeFavorite(uiModel: UiModel): Result<Boolean> {
        Timber.d("UiModel: $uiModel")
        return withContext(ioDispatcher) {
            diskDataSource.removeCharacter(uiModel.id)
            Result.success(true)
        }
    }

    override suspend fun getFavorite(): Result<List<UiModel>?> {
        Timber.d("-")
        return withContext(ioDispatcher) {
            diskDataSource.getCharacterList()
        }
    }

    override suspend fun isFavorite(uiModel: UiModel): Boolean {
        return withContext(ioDispatcher) {
            diskDataSource.getCharacter(uiModel.id).fold({
                it?.let {
                    true
                } ?: false
            }, {
                false
            })
        }
    }
}
