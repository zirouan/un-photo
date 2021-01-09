@file:Suppress("unused")
package com.zirouan.unphoto.util.extension

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.zirouan.unphoto.R
import com.zirouan.unphoto.base.BaseActivity
import com.zirouan.unphoto.util.OnNavigationResult
import com.zirouan.unphoto.util.extension.view.toastLong

enum class TransitionAnimation {
    TRANSLATE_FROM_RIGHT,
    TRANSLATE_FROM_LEFT,
    TRANSLATE_FROM_DOWN,
    TRANSLATE_FROM_UP,
    NO_ANIMATION,
    FADE
}

fun Fragment.isAtLeastLollipop(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
}

fun Fragment.isAtLeastMarshmallow(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}

fun Fragment.isAtLeastNougat(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
}

fun Fragment.isAtLeastQ(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}

fun Fragment.isAtLeastR(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
}

val Fragment.baseActivity get() = this.activity as? BaseActivity

val Fragment.appCompatActivity get() = this.activity as? AppCompatActivity

fun Fragment.showToast(message: String) = context?.toastLong(message)

fun Fragment.hideKeyboard() {
    val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun Fragment.showPopupMenu(
        anchorView: View?,
        menuRes: Int,
        onMenuItemClickListener: PopupMenu.OnMenuItemClickListener
) {
    val popup = PopupMenu(context, anchorView)
    popup.setOnMenuItemClickListener(onMenuItemClickListener)

    val inflater: MenuInflater = popup.menuInflater
    inflater.inflate(menuRes, popup.menu)

    if (isAtLeastQ()) {
        popup.setForceShowIcon(true)
    }

    popup.show()
}

//region Navigation
fun Fragment.navigate(
        directions: NavDirections,
        sharedElements: Pair<View, String>? = null
) {
    val transitionAnimation = if (sharedElements == null)
        TransitionAnimation.TRANSLATE_FROM_RIGHT
    else
        null

    navigate(directions, transitionAnimation, null, false, sharedElements)
}

fun Fragment.navigate(
        directions: NavDirections,
        clearBackStack: Boolean? = false,
        animation: TransitionAnimation? = TransitionAnimation.TRANSLATE_FROM_RIGHT
) {
    navigate(directions, animation, null, clearBackStack, null)
}

fun Fragment.navigate(
        directions: NavDirections,
        animation: TransitionAnimation? = TransitionAnimation.TRANSLATE_FROM_RIGHT,
        popUpTo: Int? = null,
        clearBackStack: Boolean? = false,
        sharedElements: Pair<View, String>? = null
) {
    val navController = NavHostFragment.findNavController(this)
    val destinationId = if (clearBackStack == true) navController.graph.id else popUpTo
    val transitionAnimation = if (sharedElements == null) animation else null
    val options = buildOptions(transitionAnimation, clearBackStack, destinationId)
    val extras = sharedElements?.let {
        FragmentNavigatorExtras(it)
    }

    navController.navigate(directions.actionId, directions.arguments, options, extras)
}

fun Fragment.navigate(
        @IdRes resId: Int,
        clearBackStack: Boolean? = false,
        animation: TransitionAnimation? = TransitionAnimation.TRANSLATE_FROM_RIGHT
) {
    navigate(resId, animation, null, clearBackStack, this)
}

//todo No futuro adicionar parâmetro destinationId,
// para quando tiver que voltar de um Fragment para outro que não seja diretamente o anterior
fun Fragment.navigate(
        @IdRes resId: Int,
        animation: TransitionAnimation? = TransitionAnimation.TRANSLATE_FROM_RIGHT,
        bundle: Bundle? = null,
        clearBackStack: Boolean? = false,
        fragment: Fragment
) {
    try {
        val navController = NavHostFragment.findNavController(fragment)
        val destinationId = if (clearBackStack == true) navController.graph.id else null
        val options = buildOptions(animation, clearBackStack, destinationId)
        navController.navigate(resId, bundle, options)
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}

fun Fragment.navigate(
        view: View,
        @IdRes resId: Int,
        animation: TransitionAnimation? = TransitionAnimation.TRANSLATE_FROM_RIGHT,
        clearBackStack: Boolean? = false
) {
    val navController = Navigation.findNavController(view)
    val destinationId = if (clearBackStack == true) navController.graph.id else null
    val options = buildOptions(animation, clearBackStack, destinationId)

    navController.navigate(resId, null, options)
}

fun <R> Fragment.navigateForResult(
        key: String,
        directions: NavDirections,
        owner: LifecycleOwner? = null,
        onNavigationResult: OnNavigationResult<R>
) {
    setNavigationResultObserver(
            key = key,
            owner = owner,
            onNavigationResult = onNavigationResult
    )

    navigate(directions)
}

fun <R> Fragment.setNavigationResult(key: String, result: R, destinationId: Int? = null) {
    val navController = NavHostFragment.findNavController(this)
    val savedStateHandle = if (destinationId != null)
        navController.getBackStackEntry(destinationId).savedStateHandle
    else
        navController.previousBackStackEntry?.savedStateHandle

    savedStateHandle?.set(key, result)
}

fun <R> Fragment.setNavigationResultObserver(
        key: String,
        owner: LifecycleOwner? = null,
        onNavigationResult: OnNavigationResult<R>
) {
    val lifecycleOwner = owner ?: this
    val navController = NavHostFragment.findNavController(this)
    navController.currentBackStackEntry?.savedStateHandle
            ?.getLiveData<R>(key)
            ?.observe(lifecycleOwner) { result ->
                navController.currentBackStackEntry?.savedStateHandle?.remove<R>(key)
                onNavigationResult.invoke(result)
            }
}

fun Fragment.popBackStack() {
    findNavController().popBackStack()
}

fun Fragment.popUpTo(@IdRes destinationId: Int) {
    findNavController().popBackStack(destinationId, false)
}

fun Fragment.navigateUp() {
    findNavController().navigateUp()
}

private fun Fragment.buildOptions(
        transitionAnimation: TransitionAnimation?,
        clearBackStack: Boolean?,
        @IdRes destinationId: Int?
): NavOptions {
    return navOptions {
        anim {
            when (transitionAnimation) {
                TransitionAnimation.TRANSLATE_FROM_RIGHT -> {
                    enter = R.anim.translate_left_enter
                    exit = R.anim.translate_left_exit
                    popEnter = R.anim.translate_right_enter
                    popExit = R.anim.translate_right_exit
                }
                TransitionAnimation.TRANSLATE_FROM_DOWN -> {
                    enter = R.anim.translate_slide_bottom_up
                    exit = R.anim.translate_no_change
                    popEnter = R.anim.translate_no_change
                    popExit = R.anim.translate_slide_bottom_down
                }
                TransitionAnimation.TRANSLATE_FROM_LEFT -> {
                    enter = R.anim.translate_right_enter
                    exit = R.anim.translate_right_exit
                    popEnter = R.anim.translate_left_enter
                    popExit = R.anim.translate_left_exit
                }
                TransitionAnimation.TRANSLATE_FROM_UP -> {
                    enter = R.anim.translate_slide_bottom_up
                    exit = R.anim.translate_no_change
                    popEnter = R.anim.translate_no_change
                    popExit = R.anim.translate_slide_bottom_down
                }
                TransitionAnimation.NO_ANIMATION -> {
                    enter = R.anim.translate_no_change
                    exit = R.anim.translate_no_change
                    popEnter = R.anim.translate_no_change
                    popExit = R.anim.translate_no_change
                }
                TransitionAnimation.FADE -> {
                    enter = R.anim.translate_fade_in
                    exit = R.anim.translate_fade_out
                    popEnter = R.anim.translate_fade_in
                    popExit = R.anim.translate_fade_out
                }
                else -> { }
            }
        }

        // To clean the stack below the current fragment,
        // you must set the 'destinationId' = navGraphId and 'inclusive' = true
        destinationId?.let {
            popUpTo(destinationId) {
                inclusive = clearBackStack == true
            }
        }
    }
}
//endregion Navigation