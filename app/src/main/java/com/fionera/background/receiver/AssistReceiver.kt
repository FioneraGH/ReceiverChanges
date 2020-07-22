package com.fionera.background.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fionera.background.util.PageUtil

/**
 * AssistReceiver
 *
 * @author fionera
 * @date 2020/7/22 in BehaviorChanges.
 */
class AssistReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
//        context.startActivity(Intent(context, TargetActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

//        PageUtil.moveTaskToFront(context)
        PageUtil.taskMoveToFront(context)
    }
}