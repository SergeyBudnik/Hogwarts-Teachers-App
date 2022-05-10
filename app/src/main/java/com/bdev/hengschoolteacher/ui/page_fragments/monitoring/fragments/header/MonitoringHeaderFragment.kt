package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.header

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.BaseFragment
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.MonitoringPageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.MonitoringPageFragmentViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_monitoring_header.*

@AndroidEntryPoint
class MonitoringHeaderFragment : BaseFragment<MonitoringHeaderFragmentViewModel>() {
    override fun provideViewModel() =
        ViewModelProvider(this).get(MonitoringHeaderFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_monitoring_header, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        providePageViewModel().getDataLiveData().observe(this) { data ->
            fragmentViewModel.setCurrentItem(tab = data.tab)
        }

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            updateView(data = data)
        }
    }

    private fun updateView(data: MonitoringHeaderFragmentData) {
        monitoringHeaderView.bind(
            currentTab = data.tab,
            hasLessonsAlert = data.hasLessonsAlert,
            hasTeachersAlert = data.hasTeachersAlert,
            hasStudentsAlert = data.hasStudentsAlert,
            tabClickAction = { tab ->
                providePageViewModel().setTab(tab = tab)
            }
        )
    }

    private fun providePageViewModel(): MonitoringPageFragmentViewModel =
        ViewModelProvider(requireParentFragment()).get(MonitoringPageFragmentViewModelImpl::class.java)
}