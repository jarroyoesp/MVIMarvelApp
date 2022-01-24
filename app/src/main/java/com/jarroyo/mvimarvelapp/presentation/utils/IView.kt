package com.jarroyo.mvimarvelapp.presentation.utils

interface IView<UiViewEffect: ViewEffect> {
    fun render(effect: UiViewEffect)
}
