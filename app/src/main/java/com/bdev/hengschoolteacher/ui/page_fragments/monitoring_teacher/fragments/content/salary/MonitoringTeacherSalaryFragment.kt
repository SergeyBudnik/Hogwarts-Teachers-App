package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.content.salary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragment
import com.bdev.hengschoolteacher.ui.page_fragments.common.content.BaseContentPageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.content.MonitoringTeacherContentFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_monitoring_teacher_salary.*

@AndroidEntryPoint
class MonitoringTeacherSalaryFragment : MonitoringTeacherContentFragment<MonitoringTeacherSalaryFragmentViewModel>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_monitoring_teacher_salary, container, false)

    override fun provideViewModel(): MonitoringTeacherSalaryFragmentViewModel =
        ViewModelProvider(this).get(MonitoringTeacherSalaryFragmentViewModelImpl::class.java)

    override fun getRootView(): View = monitoringTeacherSalaryRootView

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            monitoringTeacherSalaryContentView.init(
                teacher = data.teacher,
                teacherPayments = data.payments
            )
        }
    }
}
