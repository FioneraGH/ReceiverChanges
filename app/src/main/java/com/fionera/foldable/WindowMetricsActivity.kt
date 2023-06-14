package com.fionera.foldable

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.WindowInfoTracker
import com.fionera.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * MainActivity
 * Created by fionera on 2019-05-17 in ReceiverChanges.
 */
class WindowMetricsActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window_metrics)

        val wmApp = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var outMetrics = DisplayMetrics()
        wmApp.defaultDisplay.getMetrics(outMetrics)
        Log.d("WindowMetricsTag", "AppLevel default metric: $outMetrics")
        wmApp.defaultDisplay.getRealMetrics(outMetrics)
        Log.d("WindowMetricsTag", "AppLevel default real metric: $outMetrics")
//        illegal
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            Log.d("WindowMetrics", "AppLevel context display: ${applicationContext.display}")
//            applicationContext.display?.getMetrics(outMetrics)
//            Log.d("WindowMetrics", "AppLevel context metric: $outMetrics")
//            applicationContext.display?.getRealMetrics(outMetrics)
//            Log.d("WindowMetrics", "AppLevel context real metric: $outMetrics")
//        }



        val wmActivity = windowManager
        Log.d("WindowMetricsTag", "ActivityLevel default display: ${wmApp.defaultDisplay}")
        outMetrics = DisplayMetrics()
        wmActivity.defaultDisplay.getMetrics(outMetrics)
        Log.d("WindowMetricsTag", "ActivityLevel default metric: $outMetrics")
        wmActivity.defaultDisplay.getRealMetrics(outMetrics)
        Log.d("WindowMetricsTag", "ActivityLevel default real metric: $outMetrics")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Log.d("WindowMetricsTag", "ActivityLevel context display: $display")
            display?.getMetrics(outMetrics)
            Log.d("WindowMetricsTag", "ActivityLevel context metric: $outMetrics")
            display?.getRealMetrics(outMetrics)
            Log.d("WindowMetricsTag", "ActivityLevel context real metric: $outMetrics")
        }

        Log.d("WindowMetricsTag", "ActivityLevel resources metric: ${resources.displayMetrics}")
        Log.d("WindowMetricsTag", Settings.Secure.getInt(contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED , 0).toString())

        // WindowManager -> WindowInfoRepo -> WindowInfoRepository -> WindowInfoTracker
        val wmJetpack = WindowInfoTracker.getOrCreate(this)
        lifecycleScope.launch {
            val flow = wmJetpack.windowLayoutInfo(this@WindowMetricsActivity)
            flow.collect { info ->
                Log.d("WindowMetricsTag", "Jetpack display features: ${info.displayFeatures}")
            }
        }

        val progressBar = findViewById<ProgressBar>(R.id.progress_level)

        val drawableList = arrayOf(
            GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(Color.BLACK, Color.BLACK)).apply {
                cornerRadius = 9F
            },
            ScaleDrawable(
            GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(

                    Color.parseColor("#FF0000"),
                    Color.parseColor("#0000FF")
                )
            )
                .apply {
                    cornerRadius = 6F
                }, Gravity.START, 1F, -1F))
        val layerDrawable = LayerDrawable(drawableList)
        layerDrawable.setId(0, android.R.id.background)
        layerDrawable.setId(1, android.R.id.progress)
        progressBar.progressDrawable = layerDrawable
        progressBar.max = 100
        progressBar.progress = 60


        val textSpannable = findViewById<TextView>(R.id.text_spannable)

        val text = "@一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十"
        val ssb = SpannableStringBuilder(text).apply {
                setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        Toast.makeText(this@WindowMetricsActivity, "点击", Toast.LENGTH_SHORT).show()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = Color.RED
                    }
                }, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
//        textSpannable.addTextChangedListener { editable ->
//            Toast.makeText(this@WindowMetricsActivity, editable, Toast.LENGTH_SHORT).show()
//        }
        textSpannable.movementMethod = LinkMovementMethod.getInstance()
        textSpannable.text = ssb
        ViewUtil.fixClickConflict(textSpannable)
    }

}