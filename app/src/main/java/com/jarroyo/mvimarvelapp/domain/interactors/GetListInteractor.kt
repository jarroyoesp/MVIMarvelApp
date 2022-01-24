package com.jarroyo.mvimarvelapp.domain.interactors

import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import javax.inject.Inject

class GetListInteractor @Inject constructor(private val repository: DataRepository) {
    suspend operator fun invoke(page: Int): Result<List<UiModel>?> {
        return repository.getList(page)
    }
}
