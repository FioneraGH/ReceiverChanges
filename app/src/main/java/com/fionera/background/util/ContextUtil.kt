package com.fionera.background.util

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

object ContextUtil {
    private var mContext: Context? = null

    @get:Synchronized
    val applicationContext: Context?
        @SuppressLint("PrivateApi")
        get() {
            if (mContext == null) {
                try {
                    val application = Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null) as Application
                    mContext = application.applicationContext
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return mContext
        }
}