package com.jarroyo.mvimarvelapp.presentation

import android.app.Application
import com.jarroyo.mvimarvelapp.BuildConfig
import com.jarroyo.mvimarvelapp.presentation.utils.CustomTimberDebugTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.Forest.plant

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(CustomTimberDebugTree())
        }
        Timber.d("Init App")
    }
}
