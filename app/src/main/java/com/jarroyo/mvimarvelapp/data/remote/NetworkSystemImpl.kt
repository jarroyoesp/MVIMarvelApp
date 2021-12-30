package com.jarroyo.mvimarvelapp.data.remote

import android.content.Context
import android.net.ConnectivityManager

open class NetworkSystemImpl(private val appContext: Context) : NetworkSystem() {

    override fun isNetworkAvailable(): Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
}