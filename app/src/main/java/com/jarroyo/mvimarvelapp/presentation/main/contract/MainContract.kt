package com.jarroyo.mvimarvelapp.presentation.main.contract

import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.presentation.utils.ViewEffect
import com.jarroyo.mvimarvelapp.presentation.utils.ViewIntent
import com.jarroyo.mvimarvelapp.presentation.utils.ViewState

object MainContract {

    data class State (
        val isLoading: Boolean = false,
        var list: List<UiModel>? = emptyList(),
        val errorMessage: String? = null
    ) : ViewState

    sealed class Intent : ViewIntent {
        object FetchData : Intent()
    }

    sealed class Effect : ViewEffect {
        object InitialState: Effect()
        object ShowLoading: Effect()
        object HideLoading: Effect()
        data class ShowList(val list: List<UiModel>?): Effect()
        data class ShowError(val message: String) : Effect()
    }
}