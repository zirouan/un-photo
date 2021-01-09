package com.zirouan.unphoto.util

import android.content.Intent
import android.view.View

typealias OnAnimationFinishCallback = (() -> Unit)
typealias OnNavigationResult<R> = ((result: R) -> Unit)
typealias OnSpinnerItemSelectedListener = ((position: Int) -> Unit)
typealias OnEventReceivedListener = ((code: Int, data: Any?) -> Unit)
typealias OnItemClickListener<T> = ((view: View, position: Int, data: T) -> Unit)
typealias OnInnerViewItemClickListener = ((view: View?, position: Int, data: Any?) -> Unit)
typealias OnActivityResultCallback = ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)
