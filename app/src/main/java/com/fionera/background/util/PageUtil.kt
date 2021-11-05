package com.fionera.background.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * PageUtil
 *
 * @author fionera
 * @date 2020/7/22 in BehaviorChanges.
 */
object PageUtil {

    fun moveTaskToFront(context: Context) {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        am.run {
            getRunningTasks(3).forEach {
                if (it.id > 1000) {
                    moveTaskToFront(it.id, ActivityManager.MOVE_TASK_WITH_HOME)
                    println("MoveTaskToFront taskId:${it.id}")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun taskMoveToFront(context: Context) {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        am.run {
            appTasks.forEach { _ ->
                val task = appTasks.maxByOrNull { itInner -> itInner.taskInfo.taskId }
                task?.moveToFront()
                println("TaskMoveToFront taskId:${task?.taskInfo?.taskId}")
            }
        }
    }
}