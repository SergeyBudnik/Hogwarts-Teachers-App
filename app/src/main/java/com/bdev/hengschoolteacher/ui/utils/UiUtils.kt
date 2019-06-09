package com.bdev.hengschoolteacher.ui.utils

import android.os.Handler
import android.os.Looper

object UiUtils {
    fun runOnUi(action: () -> Unit) {
        Handler(Looper.getMainLooper()).post(action)
    }
}
