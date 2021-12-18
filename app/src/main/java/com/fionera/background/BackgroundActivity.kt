package com.fionera.background

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.fionera.background.util.DialNotificationUtil
import com.fionera.background.util.PageUtil
import com.fionera.databinding.ActivityBackgroundBinding

class BackgroundActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBackgroundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBackgroundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        println("BackgroundActivity taskId:${taskId}")

        binding.button1.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                println("reached")
                applicationContext.startActivity(Intent(this@BackgroundActivity, TargetActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
//                PageUtil.moveTaskToFront(this)
            }, 3000)
        }

        binding.button2.setOnClickListener {
            DialNotificationUtil.showOngoingNotification()
        }

        binding.button3.setOnClickListener {
            DialNotificationUtil.showOngoingNotification(true)
        }

        binding.button4.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                println("reached")
                DialNotificationUtil.showOngoingNotification(true)
            }, 3000)
        }

        binding.button5.setOnClickListener {
            DialNotificationUtil.showOneshotAssistNotification()
        }

        binding.button6.setOnClickListener {
            DialNotificationUtil.showBroadcastAssistNotification()
        }

        binding.button7.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                println("reached")
                DialNotificationUtil.showBroadcastAssistNotification()
            }, 3000)
        }

        binding.button8.setOnClickListener {
            startActivity(Intent(this, TargetActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            Handler(Looper.getMainLooper()).postDelayed({
                println("reached")
                PageUtil.moveTaskToFront(this)
//                PageUtil.taskMoveToFront(this)
//                startActivity(Intent(this, TargetActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }, 3000)
        }
    }
}