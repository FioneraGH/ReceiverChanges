package com.fionera.receiverchanges.nougat.svelte

import android.hardware.Camera
import android.net.ConnectivityManager

/**
 * ImplicitAction
 * Created by fionera on 2019-05-17 in ReceiverChanges.
 */
object ImplicitAction {
    val CONNECTIVITY_ACTION = ConnectivityManager.CONNECTIVITY_ACTION // Target N, Fail in manifest, but not Context
    val ACTION_NEW_PICTURE = Camera.ACTION_NEW_PICTURE // Run above N
    val ACTION_NEW_VIDEO = Camera.ACTION_NEW_VIDEO // Run above N

}