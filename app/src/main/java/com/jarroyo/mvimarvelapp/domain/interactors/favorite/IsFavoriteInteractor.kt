package com.jarroyo.mvimarvelapp.domain.interactors.favorite

import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.domain.repository.DataRepository
import javax.inject.Inject

class IsFavoriteInteractor @Inject constructor(private val repository: DataRepository) {
    suspend operator fun invoke(uiModel: UiModel): Boolean {
        return repository.isFavorite(uiModel)
    }
}