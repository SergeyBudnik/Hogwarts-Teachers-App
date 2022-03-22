package com.bdev.hengschoolteacher.ui.activities

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.bdev.hengschoolteacher.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HTActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ht)
    }
}