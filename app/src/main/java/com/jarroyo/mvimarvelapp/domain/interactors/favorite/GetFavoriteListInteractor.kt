package com.jarroyo.mvimarvelapp.domain.interactors.favorite

import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import javax.inject.Inject

class GetFavoriteListInteractor @Inject constructor(private val repository: DataRepository) {
    suspend operator fun invoke(): Result<List<UiModel>?> {
        return repository.getFavorite()
    }
}
