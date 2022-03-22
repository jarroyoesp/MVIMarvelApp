package com.jarroyo.mvimarvelapp.domain.interactors

import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SearchInteractor @Inject constructor(private val repository: DataRepository) {
    operator fun invoke(name: String): Flow<Result<List<UiModel>?>> {
        return repository.search(name)
    }
}
