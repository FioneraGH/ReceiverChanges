package com.fionera.motionlayout

import android.animation.*
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

        val left = PropertyValuesHolder.ofInt("left", 60, 160, 60)
        val top = PropertyValuesHolder.ofInt("top", 60, 0, 60)
        val right = PropertyValuesHolder.ofInt("right", 360, 520, 360)
        val bottom = PropertyValuesHolder.ofInt("bottom", 360, 420, 360)
        val scrollX = PropertyValuesHolder.ofInt("scrollX", 0, -30, 0)
        val scrollY = PropertyValuesHolder.ofInt("scrollY", 0, -60, 0)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            activityMotionBinding.llTransitionContainer.getChildAt(0),
            left,
            top,
            right,
            bottom,
            scrollX,
            scrollY,
        )
        animator.duration = 3000
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                println("AnimationEnd")
            }

            override fun onAnimationStart(animation: Animator) {
                println("AnimationStart")
            }

            override fun onAnimationCancel(animation: Animator) {
                println("AnimationCancel")

                activityMotionBinding.llTransitionContainer.getChildAt(0).bottom = 250
            }
        })

        activityMotionBinding.vAnimateTarget.setOnClickListener {
//            val startScene =
//                Scene.getSceneForLayout(activityMotionBinding.root, R.layout.activity_motion, this)
//            val endScene =
//                Scene.getSceneForLayout(activityMotionBinding.root, R.layout.activity_motion_end, this)
//
//            TransitionManager.go(endScene, ChangeBounds())

            activityMotionBinding.llTransitionContainer.getChildAt(0).visibility =
                if (activityMotionBinding.llTransitionContainer.getChildAt(0).isShown) View.GONE else View.VISIBLE

//            activityMotionBinding.llTransitionContainer.layoutParams.height = 500
//            activityMotionBinding.llTransitionContainer.requestLayout()

//            if (animator.isRunning) {
//                animator.cancel()
//                return@setOnClickListener
//            }
//
//            animator.setupStartValues()
//
//            animator.start()
        }

        val slide = ObjectAnimator.ofInt(activityMotionBinding.llTransitionContainer.getChildAt(0), "rotation", -300, 0)
        slide.duration = 3000

        (activityMotionBinding.llTransitionContainer as? ViewGroup)?.layoutTransition?.apply {
            setStartDelay(LayoutTransition.CHANGE_APPEARING, 3000)
            setStartDelay(LayoutTransition.APPEARING, 1000)

            setAnimator(LayoutTransition.APPEARING, slide)
            setAnimator(LayoutTransition.DISAPPEARING, slide)
        }
    }
}