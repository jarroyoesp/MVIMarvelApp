package com.jarroyo.mvimarvelapp.presentation.detail.contract

import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.presentation.utils.ViewEffect
import com.jarroyo.mvimarvelapp.presentation.utils.ViewIntent
import com.jarroyo.mvimarvelapp.presentation.utils.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.Exception

object DetailContract {

    data class State (
        var isFavorite: Boolean = false,
        var uiModel: UiModel? = null,
        val errorMessage: String? = null,
    ) : ViewState

    sealed class Intent : ViewIntent {
        data class IsFavorite(val uiModel: UiModel) : Intent()
        data class SaveFavorite(val uiModel: UiModel) : Intent()
    }

    sealed class Effect : ViewEffect {
        object InitialState: Effect()
        object ShowLoading: Effect()
        object HideLoading: Effect()
        object ShowIsFavorite: Effect()
        object ShowIsNoFavorite: Effect()
        data class  ShowSnackBar(val isFavorite: Boolean): Effect()
        data class ShowError(val throwable: Throwable) : Effect()
    }
}