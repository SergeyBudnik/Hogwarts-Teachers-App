package com.bdev.hengschoolteacher.ui.utils

import android.content.Context
import com.bdev.hengschoolteacher.R

object HeaderElementsUtils {
    fun getColor(context: Context, toggled: Boolean): Int {
        return context.resources.getColor(if (toggled) {
            R.color.fill_text_basic_action_link
        } else {
            R.color.fill_text_basic
        })
    }
}
