package com.zirouan.unphoto.base

import androidx.appcompat.app.AppCompatActivity
import com.zirouan.unphoto.util.OnEventReceivedListener

abstract class BaseActivity : AppCompatActivity() {

    private var mOnEventReceivedListeners = mutableListOf<OnEventReceivedListener>()
    private var mChangeNavigationVisibilityListener: ((Boolean) -> Unit)? = null

    var changeNavigationVisibilityListener
        get() = mChangeNavigationVisibilityListener
        set(value) {
            mChangeNavigationVisibilityListener = value
        }

    fun addOnEventReceivedListener(onEventReceivedListener: OnEventReceivedListener?) {
        onEventReceivedListener?.let { mOnEventReceivedListeners.add(it) }
    }

    fun removeOnEventReceivedListener(onEventReceivedListener: OnEventReceivedListener?) {
        onEventReceivedListener?.let {
            mOnEventReceivedListeners.remove(onEventReceivedListener)
        }
    }

    fun callOnEventReceived(code: Int, data: Any? = null) {
        mOnEventReceivedListeners.forEach {
            it.invoke(code, data)
        }
    }
}