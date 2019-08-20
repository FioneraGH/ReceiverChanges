package com.fionera.elevation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fionera.R

/**
 * MainActivity
 * Created by fionera on 2019-05-17 in ReceiverChanges.
 */
class ElevationActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elevation)
    }
}