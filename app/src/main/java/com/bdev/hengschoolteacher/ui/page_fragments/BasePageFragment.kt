package com.bdev.hengschoolteacher.ui.page_fragments

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.bdev.hengschoolteacher.ui.fragments.BaseFragment
import com.bdev.hengschoolteacher.ui.views.app.root.HtPageRootView

abstract class BasePageFragment<ViewModelType : BasePageFragmentViewModel> : BaseFragment<ViewModelType>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity().onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                fragmentViewModel.goBack()
            }
        })
    }
}
