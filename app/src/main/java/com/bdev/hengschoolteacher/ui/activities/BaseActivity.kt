package com.bdev.hengschoolteacher.ui.activities

import android.annotation.SuppressLint
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity

@SuppressLint("Registered")
open class BaseActivity : FragmentActivity() {
    override fun onStart() {
        super.onStart()

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }
}
