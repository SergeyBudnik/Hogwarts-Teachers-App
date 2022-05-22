package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.content.lessons

import androidx.lifecycle.SavedStateHandle
import com.bdev.hengschoolteacher.data.common.NullableMutableLiveDataWithState
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.MonitoringTeacherPageFragmentArguments
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.data.MonitoringTeacherPageFragmentTab
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.content.MonitoringTeacherContentFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.content.MonitoringTeacherContentFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringTeacherLessonsFragmentViewModel :
    MonitoringTeacherContentFragmentViewModel<MonitoringTeacherLessonsFragmentData>

@HiltViewModel
class MonitoringTeacherLessonsFragmentViewModelImpl @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val lessonsInteractor: LessonsInteractor
):
    MonitoringTeacherLessonsFragmentViewModel,
    MonitoringTeacherContentFragmentViewModelImpl<MonitoringTeacherLessonsFragmentData>()
{
    private var initialData = getInitialData(lessons = emptyList())

    private val dataLiveData = NullableMutableLiveDataWithState<MonitoringTeacherLessonsFragmentData>(
        initialValue = null
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun isVisible() = dataLiveData.getValue()?.visible ?: false

    override fun setArgs(args: MonitoringTeacherPageFragmentArguments) {
        val initialData = getInitialData(teacherLogin = args.teacherLogin)

        this.initialData = initialData

        dataLiveData.updateValue { initialData }
    }

    override fun setCurrentTab(tab: MonitoringTeacherPageFragmentTab) {
        dataLiveData.updateValue(defaultValue = initialData) { oldValue ->
            oldValue.copy(visible = (tab == MonitoringTeacherPageFragmentTab.LESSONS))
        }
    }

    private fun getInitialData(teacherLogin: String): MonitoringTeacherLessonsFragmentData =
        getInitialData(
            lessons = lessonsInteractor.getTeacherLessons(
                teacherLogin = teacherLogin,
                weekIndex = 0
            )
        )

    private fun getInitialData(lessons: List<GroupAndLesson>): MonitoringTeacherLessonsFragmentData =
        MonitoringTeacherLessonsFragmentData(
            visible = false,
            filterEnabled = true,
            calendarEnabled = false,
            weekIndex = 0,
            lessons = lessons
        )

    private fun getLessons(teacherLogin: String, weekIndex: Int): List<GroupAndLesson> =
        lessonsInteractor.getTeacherLessons(teacherLogin = teacherLogin, weekIndex = weekIndex)
}