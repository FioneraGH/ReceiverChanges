package com.fionera.background

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.fionera.R
import com.fionera.background.util.DialNotificationUtil
import com.fionera.background.util.PageUtil
import kotlinx.android.synthetic.main.activity_background.*

class BackgroundActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_background)

        println("BackgroundActivity taskId:${taskId}")

        button1.setOnClickListener {
            Handler().postDelayed({
                println("reached")
                applicationContext.startActivity(Intent(this@BackgroundActivity, TargetActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
//                PageUtil.moveTaskToFront(this)
            }, 3000)
        }

        button2.setOnClickListener {
            DialNotificationUtil.showOngoingNotification()
        }

        button3.setOnClickListener {
            DialNotificationUtil.showOngoingNotification(true)
        }

        button4.setOnClickListener {
            Handler().postDelayed({
                println("reached")
                DialNotificationUtil.showOngoingNotification(true)
            }, 3000)
        }

        button5.setOnClickListener {
            DialNotificationUtil.showOneshotAssistNotification()
        }

        button6.setOnClickListener {
            DialNotificationUtil.showBroadcastAssistNotification()
        }

        button7.setOnClickListener {
            Handler().postDelayed({
                println("reached")
                DialNotificationUtil.showBroadcastAssistNotification()
            }, 3000)
        }

        button8.setOnClickListener {
            startActivity(Intent(this, TargetActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            Handler().postDelayed({
                println("reached")
                PageUtil.moveTaskToFront(this)
//                PageUtil.taskMoveToFront(this)
//                startActivity(Intent(this, TargetActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }, 3000)
        }
    }
}