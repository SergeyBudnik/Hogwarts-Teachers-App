package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.content.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.lessons.LessonsListFragment
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.content.MonitoringTeacherContentFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_monitoring_teacher_lessons.*

@AndroidEntryPoint
class MonitoringTeacherLessonsFragment : MonitoringTeacherContentFragment<
    MonitoringTeacherLessonsFragmentViewModel
>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_monitoring_teacher_lessons, container, false)

    override fun provideViewModel(): MonitoringTeacherLessonsFragmentViewModel =
        ViewModelProvider(this).get(MonitoringTeacherLessonsFragmentViewModelImpl::class.java)

    override fun getRootView(): View = monitoringTeacherLessonsRootView

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        initLessonsList()
        initWeekSelectionBar()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            bindLessonsList(data = data)
        }
    }

    private fun initLessonsList() {
        monitoringTeacherLessonsListView.getFragment<LessonsListFragment>().init(
            disableFilterAction = {
                // fragmentViewModel.disableFilter()
            }
        )
    }

    private fun initWeekSelectionBar() {
        monitoringTeacherLessonsWeekSelectionBarView.init { weekIndex ->
//            fragmentViewModel.setWeekIndex(
//                weekIndex = weekIndex
//            )
        }
    }

    private fun bindLessonsList(data: MonitoringTeacherLessonsFragmentData) {
        monitoringTeacherLessonsListView.getFragment<LessonsListFragment>().update(
            lessons = data.lessons,
            filterEnabled = data.filterEnabled,
            weekIndex = data.weekIndex
        )
    }
}
