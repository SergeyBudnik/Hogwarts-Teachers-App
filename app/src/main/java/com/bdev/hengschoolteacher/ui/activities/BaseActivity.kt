package com.bdev.hengschoolteacher.ui.activities

import android.annotation.SuppressLint
import android.support.v4.app.FragmentActivity
import android.view.WindowManager

@SuppressLint("Registered")
open class BaseActivity : FragmentActivity() {
    override fun onStart() {
        super.onStart()

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }
}
