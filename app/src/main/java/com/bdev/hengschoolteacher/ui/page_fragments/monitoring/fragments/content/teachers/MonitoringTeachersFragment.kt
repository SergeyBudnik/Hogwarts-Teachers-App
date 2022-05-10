package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.teachers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.MonitoringContentFragment
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.teachers.adapters.MonitoringTeachersListAdapter
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

//            MonitoringTeacherLessonsPageFragment.redirectToChild(
//                    current = this,
//                    teacherLogin = teacher.staffMember.login
//            )
        }

        return adapter
    }

    private fun updateListView(
        adapter: MonitoringTeachersListAdapter,
        data: MonitoringTeachersFragmentData
    ) {
        adapter.setItems(data.teachers)
    }
}
