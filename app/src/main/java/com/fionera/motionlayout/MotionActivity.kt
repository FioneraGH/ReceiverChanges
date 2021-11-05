package com.fionera.motionlayout

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.transition.ChangeBounds
import androidx.transition.Scene
import android.transition.Transition
import androidx.transition.TransitionManager
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.fionera.R
import com.fionera.databinding.ActivityMotionBinding

/**
 * MainActivity
 * Created by fionera on 2019-05-17 in ReceiverChanges.
 */
class MotionActivity : AppCompatActivity() {

    private lateinit var activityMotionBinding: ActivityMotionBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMotionBinding = ActivityMotionBinding.inflate(layoutInflater)
        setContentView(activityMotionBinding.root)

//        activityMotionBinding.vAnimateTarget.viewTreeObserver.addOnPreDrawListener(object :
//            ViewTreeObserver.OnPreDrawListener {
//            override fun onPreDraw(): Boolean {
//                val animateSet = AnimatorSet()
//
//                val scaleUpXAnim: ObjectAnimator =
//                    ObjectAnimator.ofFloat(activityMotionBinding.vAnimateTarget, "scaleX", 1F, 1.3F)
//                        .setDuration(280)
//                val scaleUpYAnim: ObjectAnimator =
//                    ObjectAnimator.ofFloat(activityMotionBinding.vAnimateTarget, "scaleY", 1F, 1.3F)
//                        .setDuration(280)
//                val scaleDownXAnim: ObjectAnimator =
//                    ObjectAnimator.ofFloat(activityMotionBinding.vAnimateTarget, "scaleX", 1.3F, 1F)
//                        .setDuration(560)
//                val scaleDownYAnim: ObjectAnimator =
//                    ObjectAnimator.ofFloat(activityMotionBinding.vAnimateTarget, "scaleY", 1.3F, 1F)
//                        .setDuration(560)
//
//                val targetWidth = activityMotionBinding.llMotionContainer.width
//
//                val widthAnim = ValueAnimator.ofInt(84, targetWidth)
//                    .setDuration(560)
//
//                widthAnim.addUpdateListener {
//                    activityMotionBinding.vAnimateTarget.layoutParams.width =
//                        it.animatedValue as Int
//                    activityMotionBinding.vAnimateTarget.requestLayout()
//                }
//
//                animateSet.play(widthAnim).after(scaleDownXAnim)
//                animateSet.play(scaleDownXAnim).with(scaleDownYAnim)
//                animateSet.play(scaleDownXAnim).after(scaleUpXAnim)
//                animateSet.play(scaleUpXAnim).with(scaleUpYAnim)
//
//                animateSet.start()
//
//                activityMotionBinding.vAnimateTarget.viewTreeObserver.removeOnPreDrawListener(this)
//                return false
//            }
//        })

        activityMotionBinding.vAnimateTarget.setOnClickListener {
            val startScene =
                Scene.getSceneForLayout(activityMotionBinding.root, R.layout.activity_motion, this)
            val endScene =
                Scene.getSceneForLayout(activityMotionBinding.root, R.layout.activity_motion_end, this)

            TransitionManager.go(endScene, ChangeBounds())
        }

    }
}