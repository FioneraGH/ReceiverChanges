package com.fionera.receiverchanges.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

/**
 * Ext
 * Created by fionera on 2019-05-17 in ReceiverChanges.
 */

fun Context.showToast(msg: String) = Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()

fun postDelay(run: () -> Unit, ms: Long) = Handler(Looper.getMainLooper()).postDelayed(run, ms)