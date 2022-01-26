package java.com.jarroyo.marvel.data.repository

import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDataRepositoryImpl : DataRepository {

    companion object {

        var resultList = mutableListOf<UiModel>()
        var favoriteList = mutableListOf<UiModel>()
        var isFavorite = false

        fun reset() {
            resultList = mutableListOf()
            favoriteList = mutableListOf()
        }
    }

    override suspend fun getList(page: Int): Result<List<UiModel>?> {
        return Result.success(resultList)
    }

    override suspend fun search(name: String): Result<List<UiModel>?> {
        return Result.success(resultList)
    }

    override suspend fun saveFavorite(uiModel: UiModel): Result<Boolean> {
        return Result.success(true)
    }

    override suspend fun removeFavorite(uiModel: UiModel): Result<Boolean> {
        return Result.success(true)
    }

    override suspend fun getFavorite(): Result<List<UiModel>?> {
        return Result.success(favoriteList)
    }

    override suspend fun isFavorite(uiModel: UiModel): Boolean {
        return isFavorite
    }
}
