package com.aomiga.focus.extensions

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(msg: String?) {
    if (!msg.isNullOrBlank()) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

fun Context.toast(@StringRes msg: Int) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
