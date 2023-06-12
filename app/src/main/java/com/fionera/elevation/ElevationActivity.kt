package com.fionera.elevation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
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

        val view = findViewById<LinearLayout>(R.id.ll_button_container)
        val parent = view.parent as View

        val run = Run(parent, view)
        parent.postDelayed(run, 2000)
    }

    private class Run(val parent: View, val child: LinearLayout) : Runnable {

        override fun run() {
            println("ElevationActivity:${parent.width} wrap ${child.width}")
            if (child.width > 2000) {
                child.removeAllViews()
                return
            }
            child.addView(Button(parent.context), 300, 300)
            parent.postDelayed(this, 2000)
        }
    }
}