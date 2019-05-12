package com.vipulasri.streamer.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.vipulasri.streamer.R
import com.vipulasri.streamer.StreamerApplication
import java.io.IOException
import java.io.StringReader
import java.util.concurrent.TimeoutException

fun dpToPx(dp: Float, context: Context): Int {
    return dpToPx(dp, context.resources)
}

fun dpToPx(dp: Float, resources: Resources): Int {
    val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    return px.toInt()
}

fun inflateView(@LayoutRes layoutResId: Int, parent: ViewGroup, attachToRoot: Boolean): View {
    return LayoutInflater.from(parent.context).inflate(layoutResId, parent, attachToRoot)
}

inline fun <T:Any, R> whenNotNull(input: T?, callback: (T)->R): R? {
    return input?.let(callback)
}

fun getSnackBar(view: View, value: String): Snackbar {
    return getSnackBar(view, value, Snackbar.LENGTH_LONG)
}

fun getSnackBar(view: View, value: String, length: Int): Snackbar {
    return Snackbar.make(view, value, length)
}

fun showSnackBar(view: View, value: String) {
    getSnackBar(view, value).show()
}

fun showSnackBar(view: View, value: Int) {
    getSnackBar(view, view.context.getString(value)).show()
}

fun showSnackBar(view: View, value: Int, length: Int) {
    getSnackBar(view, view.context.getString(value), length).show()
}

fun getString(context: Context?, stringRes: Int) : String {
    return context!!.resources.getString(stringRes)
}

infix fun <T> Boolean.then(param: T): T? = if(this) param else null

fun View.setGone() {
    visibility = View.GONE
}

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun fetchErrorMessage(throwable: Throwable): String {
    return when (throwable) {
        is IOException -> getString(StreamerApplication.instance, R.string.error_msg_no_internet)
        is TimeoutException -> getString(StreamerApplication.instance, R.string.error_msg_timeout)
        else -> if(throwable.message != null) throwable.message!! else getString(StreamerApplication.instance, R.string.error_msg_unknown)
    }
}