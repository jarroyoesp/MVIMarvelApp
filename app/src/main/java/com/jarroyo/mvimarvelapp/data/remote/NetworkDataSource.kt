package com.jarroyo.mvimarvelapp.data.remote

import android.accounts.NetworkErrorException
import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.domain.model.toDomainModel
import java.io.IOException

interface NetworkDataSource {
    suspend fun getCharacterList(page: Int): Result<List<UiModel>?>
    suspend fun searchCharacterList(name: String): Result<List<UiModel>?>
}

class NetworkDataSourceImpl(
    private val apiService: ApiService,
    private val networkSystem: NetworkSystem
) : NetworkDataSource {

    companion object {
        private const val LIMIT = 20
    }

    override suspend fun getCharacterList(page: Int): Result<List<UiModel>?> {
        return if (networkSystem.isNetworkAvailable()) {
            try {
                val response =
                    apiService.getCharacterList(getOffset(page))
                if (response.isSuccessful) {
                    Result.success(response.body()?.toDomainModel())
                } else {
                    Result.failure(NetworkErrorException())
                }
            } catch (ioException: IOException) {
                Result.failure(ioException)
            }
        } else {
            Result.failure(NetworkErrorException())
        }
    }

    override suspend fun searchCharacterList(name: String): Result<List<UiModel>?> {
        return if (networkSystem.isNetworkAvailable()) {
            try {
                val response =
                    apiService.searchCharacterList(name)
                if (response.isSuccessful) {
                    Result.success(response.body()?.toDomainModel())
                } else {
                    Result.failure(NetworkErrorException())
                }
            } catch (ioException: IOException) {
                Result.failure(ioException)
            }
        } else {
            Result.failure(NetworkErrorException())
        }
    }

    private fun getOffset(page: Int) = page * LIMIT
}
