package com.jarroyo.mvimarvelapp.data.remote

abstract class NetworkSystem {
    abstract fun isNetworkAvailable(): Boolean
}