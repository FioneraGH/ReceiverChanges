package com.fionera.background

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.fionera.background.util.PageUtil

class TargetActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = TextView(this)
        view.text = "Target"
        view.textSize = 24F
        setContentView(view)

        println("TargetActivity taskId:${taskId}")

        Handler().postDelayed({
            println("back")
            moveTaskToBack(true)
        }, 1500)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        println("intent")
    }
}