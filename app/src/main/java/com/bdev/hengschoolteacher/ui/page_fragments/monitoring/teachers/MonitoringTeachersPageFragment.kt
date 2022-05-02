package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.teachers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.app_menu.AppMenuFragment
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import com.bdev.hengschoolteacher.ui.fragments.monitoring.header.MonitoringHeaderFragment
import com.bdev.hengschoolteacher.ui.fragments.monitoring.header.data.MonitoringHeaderFragmentItem
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.teachers.adapters.MonitoringTeachersListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_monitoring_teachers.*

@AndroidEntryPoint
class MonitoringTeachersPageFragment : BasePageFragment<MonitoringTeachersPageFragmentViewModel>() {
    override fun provideViewModel(): MonitoringTeachersPageFragmentViewModel =
        ViewModelProvider(this).get(MonitoringTeachersPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_monitoring_teachers, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        initHeader()
        initSecondaryHeader()
        initMenu()

        val listViewAdapter = initListView()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            updateListView(adapter = listViewAdapter, data = data)
        }
    }

    private fun initHeader() {
        monitoringTeachersHeaderView
            .setLeftButtonAction { monitoringTeachersMenuLayoutView.openMenu() }
    }

    private fun initSecondaryHeader() {
        getHeaderFragment().setCurrentItem(
            item = MonitoringHeaderFragmentItem.TEACHERS
        )
    }

    private fun initMenu() {
        getMenuFragment().setCurrentItem(item = AppMenuItem.MONITORING)
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
        data: MonitoringTeachersPageFragmentData
    ) {
        adapter.setItems(data.teachers)
    }

    private fun getHeaderFragment(): MonitoringHeaderFragment =
        childFragmentManager.findFragmentById(R.id.monitoringTeachersHeaderFragment) as MonitoringHeaderFragment

    private fun getMenuFragment(): AppMenuFragment =
        childFragmentManager.findFragmentById(R.id.appMenuFragment) as AppMenuFragment
}
