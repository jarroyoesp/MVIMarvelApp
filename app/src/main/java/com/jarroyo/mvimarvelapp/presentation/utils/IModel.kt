package com.jarroyo.mvimarvelapp.presentation.utils

import androidx.lifecycle.LiveData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface IModel<UiState: ViewState, UiIntent: ViewIntent, UiEffect : ViewEffect> {
    val intents: Channel<UiIntent>
    val state: LiveData<UiState>
    val effects: Flow<UiEffect>
}