package com.jarroyo.mvimarvelapp.presentation.main.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarroyo.mvimarvelapp.domain.interactors.GetListInteractor
import com.jarroyo.mvimarvelapp.domain.interactors.SearchInteractor
import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.presentation.main.contract.EditTextSearchState
import com.jarroyo.mvimarvelapp.presentation.main.contract.MainContract
import com.jarroyo.mvimarvelapp.presentation.utils.IModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val getListInteractor: GetListInteractor,
    private val searchInteractor: SearchInteractor
) : ViewModel(), IModel<MainContract.State, MainContract.Intent, MainContract.Effect> {

    companion object {
        @VisibleForTesting
        var DEBOUNCE = 500L
    }

    // INTENTS
    override val intents: Channel<MainContract.Intent> = Channel(Channel.UNLIMITED)

    // STATE
    private val _state: MutableLiveData<MainContract.State> =
        MutableLiveData<MainContract.State>().apply { value = MainContract.State() }
    override val state: LiveData<MainContract.State>
        get() = _state

    // EFFECTS
    private val _effect: Channel<MainContract.Effect> = Channel()
    override val effects = _effect.receiveAsFlow()

    init {
        handlerIntent()
        searchDataFlow()
    }

    private fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect { intent ->
                Timber.d("$intent")
                when (intent) {
                    MainContract.Intent.OnViewAttached -> {
                        fetchData()
                    }
                    is MainContract.Intent.OnSearchCharacter -> {
                        if (intent.text.isNotEmpty()) {
                            _state.value?.searchFlow?.emit(EditTextSearchState.Search(intent.text))
                        } else {
                            _state.value?.searchFlow?.emit(EditTextSearchState.EmptySearch)
                        }
                    }
                }
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            val result = getListInteractor.invoke(_state.value?.currentPage ?: 0)
            sendEffect { MainContract.Effect.HideLoading }
            Timber.d("result $result")
            if (result.isSuccess) {
                val list = result.getOrNull()
                if (list.isNullOrEmpty()) {
                    if (_state.value?.currentPage == 0) {
                        sendEffect { MainContract.Effect.InitialState }
                    }
                } else {
                    val currentPage = _state.value?.currentPage?.plus(1) ?: 0
                    _state.value?.list?.addAll(list)
                    updateState {
                        it.copy(
                            isLoading = false,
                            list = _state.value?.list,
                            currentPage = currentPage
                        )
                    }
                    sendEffect { MainContract.Effect.ShowPage(list) }
                }
            } else {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                }
                sendEffect {
                    MainContract.Effect.ShowError(
                        result.exceptionOrNull()?.message ?: "Something goes wrong"
                    )
                }
            }
        }
    }

    private fun searchDataFlow() {
        viewModelScope.launch {
            _state.value?.searchFlow
                ?.debounce(DEBOUNCE)
                ?.filter { searchState ->
                    when (searchState) {
                        EditTextSearchState.Init -> return@filter false
                        is EditTextSearchState.Search -> return@filter true
                        EditTextSearchState.EmptySearch -> return@filter true
                    }
                }
                ?.distinctUntilChanged()
                ?.flatMapLatest { editSearchState ->
                    when (editSearchState) {
                        EditTextSearchState.EmptySearch -> flow {
                            emit(Result.success<List<UiModel>?>(_state.value?.list))
                        }
                        EditTextSearchState.Init -> TODO()
                        is EditTextSearchState.Search -> {
                            updateState { it.copy(isLoading = true) }
                            sendEffect { MainContract.Effect.ShowLoading }
                            searchInteractor(editSearchState.query)
                        }
                    }
                }
            ?.collect { result ->
                Timber.d("$result")
                updateState { it.copy(isLoading = false) }
                sendEffect { MainContract.Effect.HideLoading }
                if (result.isSuccess) {
                    val list = result.getOrNull()
                    if (list.isNullOrEmpty()) {
                        sendEffect { MainContract.Effect.InitialState }
                    } else {
                        sendEffect { MainContract.Effect.ShowSearch(list) }
                    }
                } else {
                    sendEffect { MainContract.Effect.InitialState }
                }
            }
        }
    }

    private suspend fun updateState(
        handler: suspend (intent: MainContract.State) -> MainContract.State
    ) {
        state.value?.let {
            _state.postValue(handler(it))
        }
    }

    private fun sendEffect(effectBuilder: () -> MainContract.Effect) {
        viewModelScope.launch { _effect.send(effectBuilder()) }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
