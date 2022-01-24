package com.jarroyo.mvimarvelapp.presentation.detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarroyo.mvimarvelapp.domain.interactors.favorite.IsFavoriteInteractor
import com.jarroyo.mvimarvelapp.domain.interactors.favorite.RemoveFavoriteInteractor
import com.jarroyo.mvimarvelapp.domain.interactors.favorite.SaveFavoriteInteractor
import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.presentation.detail.contract.DetailContract
import com.jarroyo.mvimarvelapp.presentation.main.contract.FavoriteContract
import com.jarroyo.mvimarvelapp.presentation.utils.IModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
@Inject
constructor(
    private val isFavoriteInteractor: IsFavoriteInteractor,
    private val saveFavoriteInteractor: SaveFavoriteInteractor,
    private val removeFavoriteInteractor: RemoveFavoriteInteractor,
) : ViewModel(),
    IModel<DetailContract.State, DetailContract.Intent, DetailContract.Effect> {

    companion object {
        private val TAG = DetailViewModel::class.java.simpleName
    }

    // INTENTS
    override val intents: Channel<DetailContract.Intent> = Channel(Channel.UNLIMITED)

    // STATE
    private val _state: MutableLiveData<DetailContract.State> =
        MutableLiveData<DetailContract.State>().apply { value = DetailContract.State() }
    override val state: LiveData<DetailContract.State>
        get() = _state

    // EFFECTS
    private val _effect: Channel<DetailContract.Effect> = Channel()
    override val effects = _effect.receiveAsFlow()


    init {
        handlerIntent()
    }

    private fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect { intent ->
                Log.d(TAG, "[handlerIntent] $intent")
                when (intent) {
                    is DetailContract.Intent.IsFavorite -> {
                        isFavorite(intent.uiModel)
                    }
                    is DetailContract.Intent.SaveFavorite -> {
                        saveFavorite(intent.uiModel)
                    }
                }
            }
        }
    }


    private fun isFavorite(uiModel: UiModel) = viewModelScope.launch {
        val result = isFavoriteInteractor.invoke(uiModel)
        updateState { it.copy(isFavorite = result) }
        if (result) {
            sendEffect { DetailContract.Effect.ShowIsFavorite }
        } else {
            sendEffect { DetailContract.Effect.ShowIsNoFavorite }
        }
    }

    private fun saveFavorite(uiModel: UiModel) = viewModelScope.launch {

        if (state.value!!.isFavorite) {
            removeFavoriteInteractor(uiModel)
            updateState { it.copy(isFavorite = false) }
            sendEffect { DetailContract.Effect.ShowIsNoFavorite }
            sendEffect { DetailContract.Effect.ShowSnackBar(false) }
        } else {
            saveFavoriteInteractor(uiModel)
            updateState { it.copy(isFavorite = true) }
            sendEffect { DetailContract.Effect.ShowIsFavorite }
            sendEffect { DetailContract.Effect.ShowSnackBar(true) }
        }
    }

    private fun sendEffect(effectBuilder: () -> DetailContract.Effect) {
        viewModelScope.launch { _effect.send(effectBuilder()) }
    }

    private suspend fun updateState(handler: suspend (intent: DetailContract.State) -> DetailContract.State) {
        _state.postValue(handler(state.value!!))
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
