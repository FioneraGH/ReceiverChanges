package com.radar

class NativeLib {

    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'radar' library on application startup.
        init {
            System.loadLibrary("radar")
        }
    }
}