package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.header

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragment
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.views.CommonContentHeaderView
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.MonitoringTeacherPageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.data.MonitoringTeacherPageFragmentTab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_monitoring_teacher_header.*

@AndroidEntryPoint
class MonitoringTeacherHeaderFragment : CommonContentHeaderFragment<
    MonitoringTeacherPageFragmentTab,
    MonitoringTeacherHeaderFragmentViewModel
>() {
    private lateinit var teacherLogin: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_monitoring_teacher_header, container, false)

    override fun provideViewModel() =
        ViewModelProvider(this).get(MonitoringTeacherHeaderFragmentViewModelImpl::class.java)

    override fun providePageViewModel() =
        ViewModelProvider(requireParentFragment()).get(MonitoringTeacherPageFragmentViewModelImpl::class.java)

    override fun getHeaderView(): CommonContentHeaderView<MonitoringTeacherPageFragmentTab> = monitoringTeacherHeaderView

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        fragmentViewModel.bind(teacherLogin = teacherLogin)
    }

    fun bind(teacherLogin: String) {
        this.teacherLogin = teacherLogin
    }
}