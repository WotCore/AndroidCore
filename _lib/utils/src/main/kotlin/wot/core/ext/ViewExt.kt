package wot.core.ext

import android.view.View
import wot.core.utils.ClickUtils

/**
 * @author yangsn
 * @date 2025/2/17
 * @des
 */

fun View?.preventDoubleClick(listener: () -> Unit) {
    preventDoubleClick("lazy", listener)
}

fun View?.preventDoubleClick(
    tag: String,
    listener: () -> Unit
) {
    this?.setOnClickListener {
        ClickUtils.preventDoubleClick(tag, listener)
    }
}

fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

fun View?.invisible(isInvisible: Boolean = true) {
    if (isInvisible) {
        this?.visibility = View.INVISIBLE
    } else {
        this?.visibility = View.VISIBLE
    }
}

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.visible(isVisible: Boolean = true) {
    if (isVisible) {
        this?.visibility = View.VISIBLE
    } else {
        this?.visibility = View.GONE
    }
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun View?.gone(isGone: Boolean = true) {
    if (isGone) {
        this?.visibility = View.GONE
    } else {
        this?.visibility = View.VISIBLE
    }
}