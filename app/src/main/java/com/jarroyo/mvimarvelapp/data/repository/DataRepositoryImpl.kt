package com.jarroyo.mvimarvelapp.data.repository

import com.jarroyo.mvimarvelapp.data.local.DiskDataSource
import com.jarroyo.mvimarvelapp.data.remote.NetworkDataSource
import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.domain.model.toEntity
import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DataRepositoryImpl(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : DataRepository {

    override suspend fun getList(page: Int): Result<List<UiModel>?> {
        return withContext(ioDispatcher) {
            networkDataSource.getCharacterList(page)
        }
    }

    override suspend fun search(name: String): Result<List<UiModel>?> {
        return withContext(ioDispatcher) {
            networkDataSource.searchCharacterList(name)
        }
    }

    override suspend fun saveFavorite(uiModel: UiModel): Result<Boolean> {
        return withContext(ioDispatcher) {
            diskDataSource.insertCharacter(uiModel.toEntity())
            Result.success(true)

        }
    }

    override suspend fun removeFavorite(uiModel: UiModel): Result<Boolean> {
        return withContext(ioDispatcher) {
            diskDataSource.removeCharacter(uiModel.id)
            Result.success(true)
        }
    }

    override suspend fun getFavorite(): Result<List<UiModel>?> {
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

