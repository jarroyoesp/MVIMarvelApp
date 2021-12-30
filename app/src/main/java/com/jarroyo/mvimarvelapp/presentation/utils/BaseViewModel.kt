package com.jarroyo.mvimarvelapp.presentation.utils

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<UiEvent : ViewIntent, UiState : ViewState, UiEffect : ViewEffect> : ViewModel() {
/*    // State (current state of views)
    // Everything is lazy in order to be able to use SavedStateHandle as initial value
    private val initialState: UiState by lazy { provideInitialState() }
    private val _viewState: MutableState<UiState> by lazy { mutableStateOf(initialState) }
    val viewState: State<UiState> by lazy { _viewState }

    // Event (user actions)
    private val _event: MutableSharedFlow<UiEvent> = MutableSharedFlow()

    // Effect (side effects like error messages which we want to show only once)
    private val _effect: Channel<UiEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            _event.collect {
                handleEvent(it)
            }
        }
    }

    abstract fun provideInitialState(): UiState

    protected fun updateState(reducer: UiState.() -> UiState) {
        val newState = viewState.value.reducer()
        _viewState.value = newState
    }

    fun onUiEvent(event: UiEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    abstract fun handleEvent(event: UiEvent)

    protected fun sendEffect(effectBuilder: () -> UiEffect) {
        viewModelScope.launch { _effect.send(effectBuilder()) }
    }*/
}

interface ViewState

interface ViewIntent

interface ViewEffect