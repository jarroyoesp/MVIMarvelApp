package com.jarroyo.mvimarvelapp.presentation.main.contract

import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.presentation.utils.ViewEffect
import com.jarroyo.mvimarvelapp.presentation.utils.ViewIntent
import com.jarroyo.mvimarvelapp.presentation.utils.ViewState
import kotlinx.coroutines.flow.MutableStateFlow

object MainContract {

    data class State(
        val isLoading: Boolean = false,
        var list: MutableList<UiModel>? = mutableListOf(),
        val errorMessage: String? = null,
        var currentPage: Int = 0,
        var searchFlow: MutableStateFlow<EditTextSearchState> = MutableStateFlow(EditTextSearchState.Init)
    ) : ViewState

    sealed class Intent : ViewIntent {
        object FetchData : Intent()
        data class SearchData(val text: String) : Intent()
    }

    sealed class Effect : ViewEffect {
        object InitialState : Effect()
        object ShowLoading : Effect()
        object HideLoading : Effect()
        data class ShowPage(val list: List<UiModel>?) : Effect()
        data class ShowSearch(val list: List<UiModel>) : Effect()
        data class ResetList(val list: List<UiModel>) : Effect()
        data class ShowError(val message: String) : Effect()
    }
}

sealed class EditTextSearchState {
    data class Search(val query: String) : EditTextSearchState()
    object Init : EditTextSearchState()
}
