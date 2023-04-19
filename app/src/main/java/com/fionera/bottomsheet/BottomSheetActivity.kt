package com.fionera.bottomsheet

import android.animation.*
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.fionera.R
import com.fionera.databinding.ActivityBottomSheetBinding

var height = 0

/**
 * MainActivity
 * Created by fionera on 2019-05-17 in ReceiverChanges.
 */
class BottomSheetActivity : AppCompatActivity() {

    private lateinit var activityBottomSheetBinding: ActivityBottomSheetBinding

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBottomSheetBinding = ActivityBottomSheetBinding.inflate(layoutInflater)
        setContentView(activityBottomSheetBinding.root)

        activityBottomSheetBinding.root.post {
            Log.d("BottomSheet", "coordinate:${activityBottomSheetBinding.coordinatorParent.height}")
            Log.d("BottomSheet", "bottom:${activityBottomSheetBinding.linearBottomContainer.height}")
            Log.d("BottomSheet", "scroll:${activityBottomSheetBinding.scrollContentContainer.height}")
            Log.d("BottomSheet", "content:${activityBottomSheetBinding.linearContentContainer.height}")
        }

        height = resources.displayMetrics.heightPixels
        val dm = DisplayMetrics()
        display?.getRealMetrics(dm)
        val oHeight = dm.heightPixels

        // 2400
        // 2277
        // 2159 + 118/3=39.333
        Toast.makeText(this, "height:$height -> $oHeight\n" +
//                "snn:${ScreenUtil.getStatusHeight(this)} , ${ScreenUtil.getNavigationHeight(this)}", Toast.LENGTH_SHORT).show()
                "snn:${getStatusHeight()} , ${getNavigationHeight()}", Toast.LENGTH_SHORT).show()

        TestDialog(this).show()
    }

    override fun onStart() {
        super.onStart()
        val ref = this
        activityBottomSheetBinding.root.postDelayed({
            ref.window.decorView
            window?.navigationBarColor = Color.BLACK

            activityBottomSheetBinding.root.postDelayed({
                window?.navigationBarColor = Color.RED
            }, 2000)
        }, 2000)
    }

    private fun getStatusHeight(): Int {
        var statusHeight = -1
        try {
//            val clazz = Class.forName("com.android.internal.R\$dimen")
//            val obj = clazz.newInstance()
//            val height = clazz.getField("status_bar_height")[obj].toString().toInt()
            val resourceId: Int =
                resources.getIdentifier("status_bar_height", "dimen", "android")
            statusHeight = resources.getDimensionPixelSize(resourceId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return statusHeight
    }

    private fun getNavigationHeight(): Int {
        var statusHeight = -1
        try {
//            val clazz = Class.forName("com.android.internal.R\$dimen")
//            val obj = clazz.newInstance()
//            val height = clazz.getField("navigation_bar_height")[obj].toString().toInt()
            val resourceId: Int =
                resources.getIdentifier("navigation_bar_height", "dimen", "android")
            statusHeight = resources.getDimensionPixelSize(resourceId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return statusHeight
    }

    class TestDialog(context: Context) : Dialog(context, R.style.TransDialog) {

        var root: View? = null

        init {
            setContentView(R.layout.dialog_bottom_sheet_test)
            root = findViewById(R.id.frame_root)
        }

        override fun onStart() {
            super.onStart()
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height + 100)
            window?.setGravity(Gravity.BOTTOM)

            window?.navigationBarColor = Color.BLUE
//            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

            val ref = this
            window?.decorView?.postDelayed({
                ref.window?.decorView
                Toast.makeText(context, "decor wh:${window?.decorView?.width}x${window?.decorView?.height}\n" +
                        "root wh:${root?.width}x${root?.height}", Toast.LENGTH_SHORT).show()
            }, 1000)

            window?.decorView?.viewTreeObserver?.addOnGlobalLayoutListener {
                Toast.makeText(context, "ldecor wh:${window?.decorView?.width}x${window?.decorView?.height}\n" +
                        "lroot wh:${root?.width}x${root?.height}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}