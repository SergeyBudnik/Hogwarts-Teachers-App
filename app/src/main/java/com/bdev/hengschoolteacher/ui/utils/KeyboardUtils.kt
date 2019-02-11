package com.bdev.hengschoolteacher.ui.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext

@EBean
open class KeyboardUtils {
    @RootContext
    lateinit var context: Context

    fun hideKeyboard() {
        val activity = context as BaseActivity

        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity.currentFocus ?: View(activity)

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
