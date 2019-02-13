package com.bdev.hengschoolteacher.ui.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.bdev.hengschoolteacher.ui.activities.BaseActivity

object KeyboardUtils {
    fun showKeyboard(current: BaseActivity) {
        withKeyboard(current) { imm, view -> imm.showSoftInput(view, 0) }
    }

    fun hideKeyboard(current: BaseActivity) {
        withKeyboard(current) { imm, view -> imm.hideSoftInputFromWindow(view.windowToken, 0) }
    }

    private fun withKeyboard(current: BaseActivity, action: (InputMethodManager, View) -> Unit) {

        val imm = current.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = current.currentFocus ?: View(current)

        action.invoke(imm, view)
    }
}
