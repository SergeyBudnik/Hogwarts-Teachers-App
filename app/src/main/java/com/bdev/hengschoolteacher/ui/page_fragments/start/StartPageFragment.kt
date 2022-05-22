package com.bdev.hengschoolteacher.ui.page_fragments.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.views.app.root.HtPageRootView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartPageFragment : BasePageFragment<StartPageFragmentViewModel>() {
    override fun provideViewModel(): StartPageFragmentViewModel =
        ViewModelProvider(this).get(StartPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_loading, container, false) // todo: activity_start
}
