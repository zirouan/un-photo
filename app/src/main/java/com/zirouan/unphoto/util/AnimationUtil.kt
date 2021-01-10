package com.zirouan.unphoto.util

import android.animation.*
import android.view.View
import android.view.animation.*
import android.widget.TextView
import androidx.annotation.AnimRes
import androidx.constraintlayout.widget.Guideline
import com.zirouan.unphoto.util.extension.view.visible
import java.util.*

class AnimationUtil {

    companion object {

        private const val DURATION_DEFAULT = 300L

        fun incrementPercentText(
            textView: TextView,
            format: String,
            end: Int,
            duration: Long = 1000
        ) {
            val animator = ValueAnimator.ofInt(0, end)
            animator.duration = duration
            animator.addUpdateListener { animation ->
                textView.text = String.format(
                    Locale.getDefault(),
                    format, animation.animatedValue.toString()
                )
            }
            animator.start()
        }

        fun incrementNumberText(textView: TextView, end: Int, duration: Long = 1000) {
            val animator = ValueAnimator.ofInt(0, end)
            animator.duration = duration
            animator.addUpdateListener { animation ->
                textView.text = animation.animatedValue.toString()
            }
            animator.start()
        }

        fun incrementPercent(
            view: View, startCount: Float, endCount: Float, duration: Long = 300,
            onAnimationEndCallback: OnAnimationFinishCallback? = null
        ) {
            val animator = ValueAnimator()
            animator.setObjectValues(startCount, endCount)
            animator.addUpdateListener { animation ->
                val count = animation.animatedValue as Float

                if (view is Guideline) {
                    view.setGuidelinePercent(count)
                }
            }

            animator.duration = duration
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    onAnimationEndCallback?.invoke()
                }
            })
            animator.start()
        }

        fun showView(
            view: View?,
            @AnimRes animRes: Int,
            duration: Long? = DURATION_DEFAULT,
            delayMillis: Long? = 0L,
            onAnimationEndCallback: OnAnimationFinishCallback? = null
        ): Animation? {
            return animateView(
                view,
                false,
                duration,
                delayMillis,
                animRes,
                onAnimationEndCallback
            )
        }

        fun hideView(
            view: View?,
            @AnimRes animRes: Int,
            duration: Long? = DURATION_DEFAULT,
            delayMillis: Long? = 0L,
            onAnimationEndCallback: OnAnimationFinishCallback? = null
        ): Animation? {
            return animateView(
                view,
                true,
                duration,
                delayMillis,
                animRes,
                onAnimationEndCallback
            )
        }

        private fun animateView(
            view: View?,
            isVisible: Boolean,
            duration: Long? = DURATION_DEFAULT,
            delayMillis: Long? = 0L,
            @AnimRes animRes: Int,
            onAnimationEndCallback: OnAnimationFinishCallback? = null
        ): Animation? {
            if (view == null || view.context == null || duration == null || delayMillis == null) {
                view?.post { switchVisibility(view, isVisible) }
                return null
            }

            val anim = AnimationUtils.loadAnimation(
                view.context,
                animRes
            )
            view.postDelayed({
                view.clearAnimation()
                view.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                anim.duration = duration
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {

                    }

                    override fun onAnimationEnd(animation: Animation) {
                        switchVisibility(view, isVisible)
                        onAnimationEndCallback?.invoke()
                    }

                    override fun onAnimationRepeat(animation: Animation) {

                    }
                })
                view.startAnimation(anim)
            }, delayMillis)
            return anim
        }

        private fun switchVisibility(view: View?, isVisible: Boolean) {
            view?.visibility = if (isVisible) View.INVISIBLE else View.VISIBLE
        }

        fun fadeIn(view: View, duration: Long = 800, startOffset: Long = 0) {
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator()
            fadeIn.startOffset = startOffset
            fadeIn.duration = duration

            fadeIn.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                    view.visible(true)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationStart(animation: Animation?) {
                    view.visible(true)
                }
            })

            view.startAnimation(fadeIn)
        }

        fun fadeOut(view: View, duration: Long = 500, startOffset: Long = 0) {
            val fadeOut = AlphaAnimation(1f, 0f)
            fadeOut.interpolator = AccelerateInterpolator()
            fadeOut.duration = duration
            fadeOut.startOffset = startOffset
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation?) {
                    view.visible(false)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationStart(animation: Animation?) {
                    view.visible(true)
                }
            })
            view.startAnimation(fadeOut)
        }

        fun heartPulse(view: View, duration: Long = 300, repeatCount: Int = 3) {
            val scaleDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                view,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f)
            )
            scaleDown.duration = duration
            scaleDown.repeatCount = repeatCount
            scaleDown.repeatMode = ObjectAnimator.REVERSE

            scaleDown.start()
        }
    }
}