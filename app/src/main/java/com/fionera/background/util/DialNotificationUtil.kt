package com.fionera.background.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.fionera.background.TargetActivity

/**
 * DialNotificationUtil
 *
 * @author fionera
 * @date 2020/7/13 in driving_service.
 */
object DialNotificationUtil {
    private const val NOTIFY_ID = 12768
    private const val NOTIFY_CHANNEL_ID = "DEFAULT_CHANNEL"
    private const val NOTIFY_CHANNEL_NAME = "通知"
    private var ns = ContextUtil.applicationContext?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ns.getNotificationChannel(NOTIFY_CHANNEL_ID) == null) {
                val nc = NotificationChannel(NOTIFY_CHANNEL_ID, NOTIFY_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
                ns.createNotificationChannel(nc)
            }
        }
    }

    fun showOngoingNotification(isFull:Boolean = false) {
        createNotificationChannel()

        val nb = NotificationCompat.Builder(ContextUtil.applicationContext!!, NOTIFY_CHANNEL_ID)
        nb.setSmallIcon(android.R.drawable.ic_menu_call)
        nb.setContentTitle("常住通知")
        nb.setContentText("点我打开")
        nb.setAutoCancel(false)
        nb.setOngoing(true)

        val intent = Intent(ContextUtil.applicationContext, TargetActivity::class.java)
        val pi = PendingIntent.getActivity(ContextUtil.applicationContext, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (isFull) {
            nb.setFullScreenIntent(pi, true)
        } else {
            nb.setContentIntent(pi)
        }

        ns.notify(NOTIFY_ID, nb.build())
    }

    fun hideOngoingNotification() {
        ns.cancel(NOTIFY_ID)
    }
}