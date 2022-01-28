package com.jarroyo.mvimarvelapp.presentation.utils

import timber.log.Timber

class CustomTimberDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return "(${element.fileName}:${element.lineNumber})-${element.methodName}"
    }
}
