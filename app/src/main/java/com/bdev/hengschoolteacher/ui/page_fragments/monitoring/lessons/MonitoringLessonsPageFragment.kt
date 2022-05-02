package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.app_menu.AppMenuFragment
import com.bdev.hengschoolteacher.ui.fragments.lessons.LessonsFragment
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.page_fragment_monitoring_lessons.*

@AndroidEntryPoint
class MonitoringLessonsPageFragment : BasePageFragment<MonitoringLessonsPageFragmentViewModel>() {
    override fun provideViewModel() =
        ViewModelProvider(this).get(MonitoringLessonsPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.page_fragment_monitoring_lessons, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        initHeader()
        initLessonsList()
        initWeekSelectionBar()
        initMenu()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            bindHeader(data = data)
            bindLessonsList(data = data)
            bindWeekSelectionBar(data = data)
        }
    }

    private fun initHeader() {
        monitoringLessonsHeaderView.setLeftButtonAction {
            monitoringLessonsRootView.openMenu()
        }
    }

    private fun initLessonsList() {
        getLessonsFragment().init(
            disableFilterAction = {
                fragmentViewModel.toggleFilter()
            }
        )
    }

    private fun initWeekSelectionBar() {
        monitoringLessonsWeekSelectionBarView.init { weekIndex ->
            fragmentViewModel.setWeekIndex(
                weekIndex = weekIndex
            )
        }
    }

    private fun initMenu() {
        getMenuFragment().setCurrentItem(item = AppMenuItem.MONITORING)
    }

    private fun bindHeader(data: MonitoringLessonsPageFragmentData) {
        monitoringLessonsHeaderView.getFirstButtonHandler()
            .setAction(action = { fragmentViewModel.toggleFilter() })
            .setToggled(toggled = data.filterEnabled)

        monitoringLessonsHeaderView.getSecondButtonHandler()
            .setAction(action = { fragmentViewModel.toggleCalendar() } )
            .setToggled(toggled = data.calendarEnabled)
    }

    private fun bindLessonsList(data: MonitoringLessonsPageFragmentData) {
        getLessonsFragment().update(
            lessons = data.lessons,
            filterEnabled = data.filterEnabled,
            weekIndex = data.weekIndex
        )
    }

    private fun bindWeekSelectionBar(data: MonitoringLessonsPageFragmentData) {
        monitoringLessonsWeekSelectionBarView.visibility = visibleElseGone(
            visible = data.calendarEnabled
        )
    }

    private fun getMenuFragment(): AppMenuFragment =
        childFragmentManager.findFragmentById(R.id.appMenuFragment) as AppMenuFragment

    private fun getLessonsFragment(): LessonsFragment =
        childFragmentManager.findFragmentById(R.id.monitoringLessonsFragment) as LessonsFragment
}
