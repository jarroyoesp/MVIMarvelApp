package com.jarroyo.mvimarvelapp.presentation.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarroyo.mvimarvelapp.domain.interactors.favorite.GetFavoriteListInteractor
import com.jarroyo.mvimarvelapp.presentation.main.contract.FavoriteContract
import com.jarroyo.mvimarvelapp.presentation.utils.IModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FavoriteViewModel
@Inject
constructor(
    private val getFavoriteListInteractor: GetFavoriteListInteractor
) : ViewModel(), IModel<FavoriteContract.State, FavoriteContract.Intent, FavoriteContract.Effect> {

    companion object {
        private val TAG = FavoriteViewModel::class.java.simpleName
    }

    // INTENTS
    override val intents: Channel<FavoriteContract.Intent> = Channel(Channel.UNLIMITED)

    // STATE
    private val _state: MutableLiveData<FavoriteContract.State> =
        MutableLiveData<FavoriteContract.State>().apply { value = FavoriteContract.State() }
    override val state: LiveData<FavoriteContract.State>
        get() = _state

    // EFFECTS
    private val _effect: Channel<FavoriteContract.Effect> = Channel()
    override val effects = _effect.receiveAsFlow()

    init {
        handlerIntent()
    }

    private fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect { intent ->
                Log.d(TAG, "[handlerIntent] $intent")
                when (intent) {
                    FavoriteContract.Intent.FetchData -> {
                        getFavoriteList()
                    }
                }
            }
        }
    }

    private fun sendEffect(effectBuilder: () -> FavoriteContract.Effect) {
        viewModelScope.launch { _effect.send(effectBuilder()) }
    }

    /**
     * Get Favorite list
     */
    private fun getFavoriteList() = viewModelScope.launch {
        sendEffect { FavoriteContract.Effect.ShowLoading }
        val result = getFavoriteListInteractor()

        sendEffect { FavoriteContract.Effect.HideLoading }
        result.fold({
            if (it.isNullOrEmpty()) {
                sendEffect { FavoriteContract.Effect.NoFavorites }
            } else {
                sendEffect { FavoriteContract.Effect.ShowFavorites(it) }
            }
        }, {
            sendEffect { FavoriteContract.Effect.ShowError(it) }
        })
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
