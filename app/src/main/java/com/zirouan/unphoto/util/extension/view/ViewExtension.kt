package com.zirouan.unphoto.util.extension.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.zirouan.unphoto.R
import com.zirouan.unphoto.util.DateUtil
import com.zirouan.unphoto.util.OnSpinnerItemSelectedListener
import java.text.NumberFormat


//region Context
fun Context.toastShort(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Context.toastShort(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()

fun Context.toastLong(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_LONG).show()

fun Context.toastLong(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
//endregion Context

//region View
fun View?.color(@ColorRes resIdFrom: Int, @ColorRes resIdTo: Int) {
    this?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val anim =
                    ValueAnimator.ofArgb(
                            ContextCompat.getColor(context, resIdFrom),
                            ContextCompat.getColor(context, resIdTo)
                    )

            anim.addUpdateListener { animation ->
                setBackgroundColor(animation.animatedValue as Int)
            }
            anim.start()
        } else {
            setBackgroundColor(ContextCompat.getColor(context, resIdTo))
        }
    }
}

fun View?.color(@ColorRes resId: Int) {
    this?.let {
        setBackgroundColor(ContextCompat.getColor(context, resId))
    }
}

fun ImageView?.tint(@ColorRes resId: Int) {
    this?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val wrappedDrawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(wrappedDrawable,
                    ContextCompat.getColor(context, resId))
        } else {
            colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(context, resId),
                    PorterDuff.Mode.SRC_ATOP)
        }
    }
}

fun ImageView?.tint(@ColorRes resIdFrom: Int, @ColorRes resIdTo: Int) {
    this?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val wrappedDrawable = DrawableCompat.wrap(drawable)
            val anim =
                    ValueAnimator.ofArgb(
                            ContextCompat.getColor(context, resIdFrom),
                            ContextCompat.getColor(context, resIdTo)
                    )

            anim.addUpdateListener { animation ->
                wrappedDrawable?.let {
                    DrawableCompat.setTint(wrappedDrawable, animation.animatedValue as Int)
                }
            }
            anim.duration = 3000
            anim.start()
        } else {
            tint(resIdTo)
        }
    }
}
//endregion View

//region TextView
fun AppCompatTextView?.setCurrency(value: Double?) {
    this?.text = value?.let { NumberFormat.getCurrencyInstance().format(value) } ?: run { "-" }
}

fun AppCompatTextView?.setDate(@StringRes formatRes: Int, date: String?) {
    this?.text = date?.let {
        val formattedDate = DateUtil.formatDate(
                it, DateUtil.DATE_TIME_FORMAT_API, DateUtil.DATE_FORMAT_LOCAL
        )

        this?.context?.resources?.getString(formatRes, formattedDate)
    } ?: run {
        ""
    }
}

@Suppress("DEPRECATION")
fun AppCompatTextView?.setHtml(htmlText: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this?.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    } else {
        this?.text = Html.fromHtml(htmlText)
    }
}
//endregion TextView

//region ImageView
fun AppCompatImageView?.loadImage(urlImage: String?) {
    this?.let {
        val url = urlImage ?: ""

        Glide.with(this.context)
                .load(url)
                .into(this)
    }
}

fun AppCompatImageView?.loadImage(@DrawableRes drawableRes: Int) {
    this?.let {
        Glide.with(this.context)
                .load(drawableRes)
                .into(this)
    }
}

fun AppCompatImageView?.loadImageRounded(urlImage: String?) {
    this?.let {
        val url = urlImage ?: ""

        Glide.with(it.context).load(url).apply(
                RequestOptions.circleCropTransform()
                        .centerCrop()
                        .optionalCircleCrop()
        ).into(it)
    }
}

fun AppCompatImageView?.loadImageRounded(@DrawableRes drawableRes: Int) {
    this?.let {
        Glide.with(it.context).load(drawableRes).apply(
                RequestOptions.circleCropTransform()
                        .centerCrop()
                        .optionalCircleCrop()
        ).into(it)
    }
}

fun AppCompatImageView?.loadImage(
        url: String,
        @DrawableRes placeholderError: Int = R.drawable.ic_android,
        onFinished: (success: Boolean) -> Unit = {}
) {
    this?.let { imageView ->
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
            ): Boolean {
                onFinished(false)
                return false
            }

            override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
            ): Boolean {
                onFinished(false)
                return false
            }
        }

        Glide.with(imageView.context)
                .load(url)
                .apply(
                        RequestOptions
                                .bitmapTransform(RoundedCorners(24))
                                .error(placeholderError)
                                .centerCrop()
                )
                .listener(listener)
                .into(imageView)
    }
}

fun AppCompatImageView?.loadImageRounded(
        urlImage: String?,
        @DrawableRes placeholderError: Int,
        onLoadingFinished: (success: Boolean) -> Unit = {}
) {
    this?.let {
        val url = urlImage ?: ""

        val requestListener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
            ): Boolean {
                onLoadingFinished(false)
                return false
            }

            override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
            ): Boolean {
                onLoadingFinished(true)
                return false
            }
        }

        Glide.with(it.context)
                .load(url)
                .apply(
                        RequestOptions.circleCropTransform()
                                .centerCrop()
                                .optionalCircleCrop()
                                .error(placeholderError)
                )
                .listener(requestListener)
                .into(it)
    }
}
//endregion ImageView

//region Visible
fun View.visible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.inVisible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}
//endregion Visible

fun TextView.colorSpan(resText: String, start: Int, end: Int, color: Int = Color.BLACK) {
    val span = SpannableStringBuilder(resText)

    span.setSpan(
            ForegroundColorSpan(color), start, end,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
    )

    span.setSpan(
            StyleSpan(Typeface.BOLD), start, end,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
    )

    text = span
}

//region RecyclerView
fun RecyclerView?.enableVerticalIndicators() {
    this?.let { recyclerView ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val indicators = View.SCROLL_INDICATOR_TOP or View.SCROLL_INDICATOR_BOTTOM
            recyclerView.setScrollIndicators(
                    indicators,
                    View.SCROLL_INDICATOR_TOP or View.SCROLL_INDICATOR_BOTTOM
            )
        }
    }
}
//endregion RecyclerView

//region Spinner
fun Spinner?.setOnItemSelectedListener(listener: OnSpinnerItemSelectedListener) {
    this?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {}

        override fun onItemSelected(p0: AdapterView<*>?, v: View?, position: Int, id: Long) {
            listener.invoke(position)
        }
    }
}
//endregion Spinner
