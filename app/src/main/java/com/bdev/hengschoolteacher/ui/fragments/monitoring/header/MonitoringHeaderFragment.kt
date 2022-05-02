package com.bdev.hengschoolteacher.ui.fragments.monitoring.header

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.BaseFragment
import com.bdev.hengschoolteacher.ui.fragments.monitoring.header.data.MonitoringHeaderFragmentItem
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

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            updateView(data = data)
        }
    }

    fun setCurrentItem(item: MonitoringHeaderFragmentItem) {
        fragmentViewModel.setCurrentItem(item = item)
    }

    private fun updateView(data: MonitoringHeaderFragmentData) {
        monitoringHeaderView.bind(
            currentItem = data.currentItem,
            hasLessonsAlert = data.hasLessonsAlert,
            hasTeachersAlert = data.hasTeachersAlert,
            hasStudentsAlert = data.hasStudentsAlert,
            navCommandHandler = { navCommand ->
                fragmentViewModel.navigate(navCommand = navCommand)
            }
        )
    }
}