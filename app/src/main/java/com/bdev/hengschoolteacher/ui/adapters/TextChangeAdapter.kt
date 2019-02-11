package com.bdev.hengschoolteacher.ui.adapters

import android.text.Editable
import android.text.TextWatcher

abstract class TextChangeAdapter : TextWatcher {
    override fun afterTextChanged(v: Editable) {
        /* Do nothing */
    }

    override fun beforeTextChanged(value: CharSequence, p1: Int, p2: Int, p3: Int) {
        /* Do nothing */
    }

    override fun onTextChanged(value: CharSequence, p1: Int, p2: Int, p3: Int) {
        onTextChanged(value.toString())
    }

    abstract fun onTextChanged(value: String)
}
