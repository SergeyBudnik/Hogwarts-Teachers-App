package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.header

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.BaseFragment
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragment
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.views.CommonContentHeaderView
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.MonitoringPageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.MonitoringPageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.data.MonitoringPageFragmentTab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_monitoring_header.*

@AndroidEntryPoint
class MonitoringHeaderFragment : CommonContentHeaderFragment<
    MonitoringPageFragmentTab,
    MonitoringHeaderFragmentViewModel
>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_monitoring_header, container, false)

    override fun provideViewModel() =
        ViewModelProvider(this).get(MonitoringHeaderFragmentViewModelImpl::class.java)

    override fun providePageViewModel() =
        ViewModelProvider(requireParentFragment()).get(MonitoringPageFragmentViewModelImpl::class.java)

    override fun getHeaderView(): CommonContentHeaderView<MonitoringPageFragmentTab> = monitoringHeaderView
}