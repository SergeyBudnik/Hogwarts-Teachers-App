package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.teachers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.NavGraphDirections
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.MonitoringContentFragment
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.teachers.adapters.MonitoringTeachersListAdapter
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.MonitoringTeacherPageFragmentArguments
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_monitoring_teachers.*

@AndroidEntryPoint
class MonitoringTeachersFragment : MonitoringContentFragment<MonitoringTeachersPageFragmentViewModel>() {
    override fun provideViewModel(): MonitoringTeachersPageFragmentViewModel =
        ViewModelProvider(this).get(MonitoringTeachersPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_monitoring_teachers, container, false)

    override fun getRootView(): View = monitoringTeachersRootView

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        val listViewAdapter = initListView()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            updateListView(adapter = listViewAdapter, data = data)
        }
    }

    private fun initListView(): MonitoringTeachersListAdapter {
        val adapter = MonitoringTeachersListAdapter(requireContext())

        monitoringTeachersListView.adapter = adapter

        monitoringTeachersListView.setOnItemClickListener { _, _, position, _ ->
            val teacher = adapter.getItem(position)

            fragmentViewModel.navigate(
                navCommand = NavCommand.forward(
                    navDir = NavGraphDirections.toMonitoringTeacherAction(
                        args = MonitoringTeacherPageFragmentArguments(
                            teacherLogin = teacher.staffMember.login
                        )
                    )
                )
            )
        }

        return adapter
    }

    private fun updateListView(
        adapter: MonitoringTeachersListAdapter,
        data: MonitoringTeachersFragmentData
    ) {
        adapter.setItems(data.teachers)
        adapter.notifyDataSetChanged()
    }
}
