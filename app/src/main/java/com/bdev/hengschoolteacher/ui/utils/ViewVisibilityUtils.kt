package com.bdev.hengschoolteacher.ui.utils

import android.view.View

object ViewVisibilityUtils {
    fun visibleElseGone(visible: Boolean) = if (visible) { View.VISIBLE } else { View.GONE }
}