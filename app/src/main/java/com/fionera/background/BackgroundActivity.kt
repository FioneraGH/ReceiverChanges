package com.fionera.background

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.fionera.R
import com.fionera.background.util.DialNotificationUtil
import kotlinx.android.synthetic.main.activity_background.*

class BackgroundActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_background)

        button1.setOnClickListener {
            Handler().postDelayed(Runnable {
                println("reached")
                applicationContext.startActivity(Intent(this@BackgroundActivity, TargetActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }, 3000)
        }

        button2.setOnClickListener {
            DialNotificationUtil.showOngoingNotification()
        }

        button3.setOnClickListener {
            DialNotificationUtil.showOngoingNotification(true)
        }


        button4.setOnClickListener {
            Handler().postDelayed(Runnable {
                println("reached")
                DialNotificationUtil.showOngoingNotification(true)
            }, 3000)
        }
    }
}