package com.zirouan.unphoto.util.widget

import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.annotation.AnimRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.zirouan.unphoto.R

class NavigationView : ConstraintLayout {

    private var mSelectedViewId: Int = 0

    private var mContainer: ViewGroup? = null
    private var mOnNavigationItemSelected: OnNavigationItemSelectedListener? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (!isInEditMode) {
            if (attrs != null) {
                val tpArray = context.obtainStyledAttributes(
                    attrs, R.styleable.NavigationView
                )
                val layout = tpArray.getResourceId(
                    R.styleable.NavigationView_layout, 0
                )

                tpArray.recycle()

                if (layout == 0) {
                    throw RuntimeException(
                        "No layout reference, please include attribute "
                                + "'app:layout' in XML, to use this component"
                    )
                }

                val navContainer = LayoutInflater.from(context).inflate(
                    layout, this,
                    true
                ) as ViewGroup

                mContainer = navContainer//.getChildAt(0) as ViewGroup
                (mContainer?.getChildAt(0) as? ViewGroup)?.getChildAt(0)?.id?.let { id ->
                    mSelectedViewId = id
                    changeViewTint(mContainer)
                }
                setChildrenListener(mContainer)
            }
        }
    }

    private fun setChildrenListener(container: ViewGroup?) {
        container?.let { cont ->
            if (cont.childCount > 0 && cont.getChildAt(0) is ViewGroup) {
                setChildrenListener(cont.getChildAt(0) as ViewGroup)
            } else {
                for (i in 0 until cont.childCount) {
                    val child = cont.getChildAt(i)
                    child?.setOnClickListener { this.onClick(it) }
                }
            }
        }
    }

    fun onClick(view: View) {
        mSelectedViewId = view.id

        mOnNavigationItemSelected?.onNavigationItemSelected(view)

        changeViewTint(mContainer)
    }

    private fun changeViewTint(container: ViewGroup?) {
        container?.let { cont ->
            if (cont.childCount > 0 && cont.getChildAt(0) is ViewGroup) {
                changeViewTint(cont.getChildAt(0) as ViewGroup)
            } else {
                for (i in 0 until cont.childCount) {
                    val child = cont.getChildAt(i)

                    child?.let {
                        if (child is AppCompatTextView) {

                            child.isSelected = child.id == mSelectedViewId
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun changeAnimatedBackground(view: View) {
        val anim = ValueAnimator.ofArgb(
            ContextCompat.getColor(context, R.color.colorPrimaryVariant),
            ContextCompat.getColor(context, R.color.colorPrimary)
        )
        anim.addUpdateListener { animation ->
            view.setBackgroundColor(animation.animatedValue as Int)
        }
        anim.start()
    }

    fun animateItems(@AnimRes animRes: Int) {
        val controller = AnimationUtils.loadLayoutAnimation(context, animRes)

        mContainer?.layoutAnimation = controller
        mContainer?.scheduleLayoutAnimation()
        mContainer?.clearAnimation()
    }

    fun setOnNavigationItemSelected(onNavigationItemSelected: OnNavigationItemSelectedListener) {
        this.mOnNavigationItemSelected = onNavigationItemSelected
    }

    fun setSelectedItemId(itemId: Int) {
        mSelectedViewId = itemId
        changeViewTint(mContainer)

        if (mContainer != null) {
            mOnNavigationItemSelected.let {
                it?.onNavigationItemSelected(mContainer!!.findViewById(itemId))
            }
        }
    }

    val selectedViewId get() = mSelectedViewId

    fun setItemDrawable(itemId: Int, @DrawableRes drawableRes: Int) {
        val imageView = mContainer!!.findViewById<ImageView>(itemId)

        imageView?.setImageResource(drawableRes)
    }

    interface OnNavigationItemSelectedListener {

        fun onNavigationItemSelected(item: View)
    }
}