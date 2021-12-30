package com.jarroyo.mvimarvelapp.presentation.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarroyo.mvimarvelapp.domain.interactors.GetListInteractor
import com.jarroyo.mvimarvelapp.domain.interactors.SearchInteractor
import com.jarroyo.mvimarvelapp.presentation.main.contract.EditTextSearchState
import com.jarroyo.mvimarvelapp.presentation.main.contract.MainContract
import com.jarroyo.mvimarvelapp.presentation.utils.IModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val getListInteractor: GetListInteractor,
    private val searchInteractor: SearchInteractor,
) : ViewModel(), IModel<MainContract.State, MainContract.Intent, MainContract.Effect> {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
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

    private var jobSearch: Job? = null

    init {
        handlerIntent()
        searchDataFlow()
    }

    private fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect { intent ->
                Log.d(TAG, "[handlerIntent] $intent")
                when (intent) {
                    MainContract.Intent.FetchData -> {
                        fetchData()
                    }
                    is MainContract.Intent.SearchData -> {
                        _state.value?.searchFlow?.emit(EditTextSearchState.Search(intent.text))
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
            Log.d(TAG, "[fetchData] result $result")
            if (result.isSuccess) {
                val list = result.getOrNull()
                list?.let {
                    var currentPage = _state.value?.currentPage?.plus(1) ?: 0
                    _state.value?.list?.addAll(it)
                    updateState { it.copy(isLoading = false, list = _state.value?.list, currentPage = currentPage  ) }
                    sendEffect { MainContract.Effect.ShowPage(list) }
                }

            } else {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                }
                sendEffect { MainContract.Effect.ShowError(result.exceptionOrNull()?.message ?: "Something goes wrong") }
            }
        }
    }


    private fun searchDataFlow() {
        viewModelScope.launch {
            _state.value?.searchFlow?.debounce(500)
            ?.distinctUntilChanged()
            ?.collect {
                when (it) {
                    EditTextSearchState.Init -> {
                    }
                    is EditTextSearchState.Search -> {
                        if (it.query.isNullOrEmpty()) {
                            Log.d(TAG, "[searchDataFlow] isNullOrEmpty")
                            jobSearch?.cancel()
                            state.value?.list?.let {
                                sendEffect { MainContract.Effect.ResetList(it) }
                            } ?: sendEffect { MainContract.Effect.InitialState }

                        } else {
                            Log.d(TAG, "[searchDataFlow] $it")
                            search(it.query)
                        }
                    }
                }
            }
        }
    }

    /**
     * Search
     */
    private fun search(name: String) {
        jobSearch?.cancel()
        jobSearch = viewModelScope.launch{
            updateState { it.copy(isLoading = true) }
            sendEffect { MainContract.Effect.ShowLoading }
            val result = searchInteractor.invoke(name)

            sendEffect { MainContract.Effect.HideLoading }
            Log.d(TAG, "[search] result $result")
            if (result.isSuccess) {
                val list = result.getOrNull()
                if (list.isNullOrEmpty()) {
                    sendEffect { MainContract.Effect.InitialState }
                } else {
                    sendEffect { MainContract.Effect.ShowSearch(list) }
                }
            }
        }
    }

    private suspend fun updateState(handler: suspend (intent: MainContract.State) -> MainContract.State) {
        _state.postValue(handler(state.value!!))
    }

    private fun sendEffect(effectBuilder: () -> MainContract.Effect) {
        viewModelScope.launch { _effect.send(effectBuilder()) }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}