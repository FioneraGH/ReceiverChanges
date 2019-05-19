package com.fionera.receiverchanges.oreo.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fionera.receiverchanges.util.showToast

/**
 * RegisterDynamicReceiver
 * Created by fionera on 2019-05-19 in ReceiverChanges.
 */
class RegisterDynamicReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val msg = "Manifest Static Received" +
                ": ${intent?.action} " +
                "/ ${intent?.`package`} " +
                "/ ${intent?.component}}"
        context?.showToast(msg)
        println(msg)
    }
}