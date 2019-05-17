package com.fionera.receiverchanges.nougat.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fionera.receiverchanges.util.showToast

/**
 * ManifestConnectivityReceiver
 * Created by fionera on 2019-05-17 in ReceiverChanges.
 */
class ManifestConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val msg = "Manifest Connectivity Received: ${intent?.action}"
        context?.showToast(msg)
        println(msg)
    }
}