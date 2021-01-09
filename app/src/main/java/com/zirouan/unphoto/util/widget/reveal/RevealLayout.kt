package com.zirouan.unphoto.util.widget.reveal

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import kotlin.math.max
import kotlin.math.sqrt

class RevealLayout : FrameLayout {

    companion object {
        private const val DEFAULT_DURATION = 600
    }

    private var mClipPath: Path? = null
    private var mClipRadius = 0f
    private var mClipCenterX = 0
    private var mClipCenterY = 0
    private var mAnimation: Animation? = null
    private var mIsContentShown = true

    init {
        mClipPath = Path()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        mClipCenterX = w / 2
        mClipCenterY = h / 2
        mClipRadius = if (!mIsContentShown) {
            0f
        } else {
            (Math.sqrt((w * w + h * h).toDouble()) / 2).toFloat()
        }

        super.onSizeChanged(w, h, oldw, oldh)
    }

    fun getClipRadius(): Float {
        return mClipRadius
    }

    fun setClipRadius(clipRadius: Float) {
        mClipRadius = clipRadius
        invalidate()
    }

    fun isContentShown(): Boolean {
        return mIsContentShown
    }

    fun setContentShown(isContentShown: Boolean) {
        mIsContentShown = isContentShown
        if (mIsContentShown) {
            mClipRadius = 0f
        } else {
            mClipRadius = getMaxRadius(mClipCenterX, mClipCenterY)
        }
        invalidate()
    }

    fun show() {
        show(DEFAULT_DURATION)
    }

    fun show(duration: Int) {
        show(duration, null)
    }

    fun show(x: Int, y: Int) {
        show(x, y, DEFAULT_DURATION, null)
    }

    fun show(@Nullable listener: Animation.AnimationListener) {
        show(DEFAULT_DURATION, listener)
    }

    fun show(duration: Int, @Nullable listener: Animation.AnimationListener?) {
        show(width / 2, height / 2, duration, listener)
    }

    fun show(x: Int, y: Int, @Nullable listener: Animation.AnimationListener) {
        show(x, y, DEFAULT_DURATION, listener)
    }

    fun show(x: Int, y: Int, duration: Int) {
        show(x, y, duration, null)
    }

    fun show(x: Int, y: Int, duration: Int, @Nullable listener: Animation.AnimationListener?) {
        if (x < 0 || x > width || y < 0 || y > height) {
            throw RuntimeException("Center point out of range or call method when View is not initialed yet.")
        }

        mClipCenterX = x
        mClipCenterY = y
        val maxRadius = getMaxRadius(x, y)

        clearAnimation()

        mAnimation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                setClipRadius(interpolatedTime * maxRadius)
            }
        }

        mAnimation?.let {
            it.interpolator = BakedBezierInterpolator()
            it.duration = duration.toLong()
            it.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation) {
                    listener?.onAnimationRepeat(animation)
                }

                override fun onAnimationStart(animation: Animation) {
                    mIsContentShown = true
                    listener?.onAnimationStart(animation)
                }

                override fun onAnimationEnd(animation: Animation) {
                    listener?.onAnimationEnd(animation)
                }
            })
            startAnimation(it)
        }
    }

    fun hide() {
        hide(DEFAULT_DURATION)
    }

    fun hide(duration: Int) {
        hide(width / 2, height / 2, duration, null)
    }

    fun hide(x: Int, y: Int) {
        hide(x, y, DEFAULT_DURATION, null)
    }

    fun hide(@Nullable listener: Animation.AnimationListener) {
        hide(DEFAULT_DURATION, listener)
    }

    fun hide(duration: Int, @Nullable listener: Animation.AnimationListener) {
        hide(width / 2, height / 2, duration, listener)
    }

    fun hide(x: Int, y: Int, @Nullable listener: Animation.AnimationListener) {
        hide(x, y, DEFAULT_DURATION, listener)
    }

    fun hide(x: Int, y: Int, duration: Int) {
        hide(x, y, duration, null)
    }

    fun hide(x: Int, y: Int, duration: Int, @Nullable listener: Animation.AnimationListener?) {
        if (x < 0 || x > width || y < 0 || y > height) {
            throw RuntimeException("Center point out of range or call method when View is not initialed yet.")
        }

        if (x != mClipCenterX || y != mClipCenterY) {
            mClipCenterX = x
            mClipCenterY = y
            mClipRadius = getMaxRadius(x, y)
        }

        clearAnimation()

        mAnimation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                setClipRadius(getClipRadius() * (1 - interpolatedTime))
            }
        }

        mAnimation?.let {
            it.interpolator = BakedBezierInterpolator()
            it.duration = duration.toLong()
            it.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    mIsContentShown = false
                    listener?.onAnimationStart(animation)
                }

                override fun onAnimationRepeat(animation: Animation) {
                    listener?.onAnimationRepeat(animation)
                }

                override fun onAnimationEnd(animation: Animation) {
                    listener?.onAnimationEnd(animation)
                }
            })
            startAnimation(it)
        }
    }

    operator fun next() {
        next(DEFAULT_DURATION)
    }

    fun next(duration: Int) {
        next(width / 2, height / 2, duration, null)
    }

    fun next(x: Int, y: Int) {
        next(x, y, DEFAULT_DURATION, null)
    }

    fun next(@Nullable listener: Animation.AnimationListener) {
        next(DEFAULT_DURATION, listener)
    }

    fun next(duration: Int, @Nullable listener: Animation.AnimationListener) {
        next(width / 2, height / 2, duration, listener)
    }

    fun next(x: Int, y: Int, @Nullable listener: Animation.AnimationListener) {
        next(x, y, DEFAULT_DURATION, listener)
    }

    fun next(x: Int, y: Int, duration: Int) {
        next(x, y, duration, null)
    }

    fun next(x: Int, y: Int, duration: Int, @Nullable listener: Animation.AnimationListener?) {
        val childCount = childCount
        if (childCount > 1) {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (i == 0) {
                    bringChildToFront(child)
                }
            }
            show(x, y, duration, listener)
        }
    }

    private fun getMaxRadius(x: Int, y: Int): Float {
        val h = max(x, width - x)
        val v = max(y, height - y)
        return sqrt((h * h + v * v).toDouble()).toFloat()
    }

    override fun drawChild(@NonNull canvas: Canvas, @NonNull child: View, drawingTime: Long): Boolean {
        return if (indexOfChild(child) == childCount - 1
                && mClipPath != null) {
            mClipPath!!.reset()
            mClipPath!!.addCircle(mClipCenterX.toFloat(), mClipCenterY.toFloat(),
                    mClipRadius, Path.Direction.CW)
            canvas.save()
            canvas.clipPath(mClipPath!!)
            val result = super.drawChild(canvas, child, drawingTime)
            canvas.restore()

            result
        } else {
            super.drawChild(canvas, child, drawingTime)
        }
    }
}