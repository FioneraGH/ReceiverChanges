package com.fionera.background

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class NotifyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
        startActivity(Intent(this, TargetActivity::class.java))
    }
}