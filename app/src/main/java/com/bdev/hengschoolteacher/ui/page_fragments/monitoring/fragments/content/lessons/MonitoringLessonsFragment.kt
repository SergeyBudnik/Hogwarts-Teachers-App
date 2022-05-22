package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.lessons.LessonsListFragment
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.MonitoringContentFragment
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_monitoring_lessons.*

@AndroidEntryPoint
class MonitoringLessonsFragment : MonitoringContentFragment<MonitoringLessonsFragmentViewModel>() {
    override fun provideViewModel() =
        ViewModelProvider(this).get(MonitoringLessonsFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_monitoring_lessons, container, false)

    override fun getRootView(): View = monitoringLessonsRootView

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        initLessonsList()
        initWeekSelectionBar()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            bindLessonsList(data = data)
            bindWeekSelectionBar(data = data)
        }
    }

    private fun initLessonsList() {
        monitoringLessonsListView.getFragment<LessonsListFragment>().init(
            disableFilterAction = {
                fragmentViewModel.disableFilter()
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

    private fun bindLessonsList(data: MonitoringLessonsFragmentData) {
        monitoringLessonsListView.getFragment<LessonsListFragment>().update(
            lessons = data.lessons,
            filterEnabled = data.filterEnabled,
            weekIndex = data.weekIndex
        )
    }

    private fun bindWeekSelectionBar(data: MonitoringLessonsFragmentData) {
        monitoringLessonsWeekSelectionBarView.visibility = visibleElseGone(
            visible = data.calendarEnabled
        )
    }
}
