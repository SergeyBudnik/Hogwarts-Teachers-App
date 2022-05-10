package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.lessons

import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.data.MonitoringPageFragmentTab
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.MonitoringContentFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.MonitoringContentFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringLessonsPageFragmentViewModel : MonitoringContentFragmentViewModel<MonitoringLessonsFragmentData> {
    fun setWeekIndex(weekIndex: Int)

    fun disableFilter()
}

@HiltViewModel
class MonitoringLessonsPageFragmentViewModelImpl @Inject constructor(
    private val lessonsInteractor: LessonsInteractor
): MonitoringLessonsPageFragmentViewModel, MonitoringContentFragmentViewModelImpl<MonitoringLessonsFragmentData>() {
    private val initialData = MonitoringLessonsFragmentData(
        visible = false,
        filterEnabled = false,
        calendarEnabled = false,
        weekIndex = 0,
        lessons = getLessons(weekIndex = 0)
    )

    private val dataLiveData = MutableLiveDataWithState(initialValue = initialData)

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun isVisible() = dataLiveData.getValue()?.visible ?: false

    override fun setCurrentTab(tab: MonitoringPageFragmentTab) {
        dataLiveData.updateValue(defaultValue = initialData) { oldValue ->
            oldValue.copy(visible = (tab == MonitoringPageFragmentTab.LESSONS))
        }
    }

    override fun onHeaderButton1Clicked() {
        super.onHeaderButton1Clicked()

        dataLiveData.updateValue(defaultValue = initialData) { oldValue ->
            oldValue.copy(
                filterEnabled = !oldValue.filterEnabled,
                lessons = getLessons(
                    weekIndex = oldValue.weekIndex
                )
            )
        }
    }

    override fun onHeaderButton2Clicked() {
        super.onHeaderButton2Clicked()

        dataLiveData.updateValue(defaultValue = initialData) { oldValue ->
            oldValue.copy(
                calendarEnabled = !oldValue.calendarEnabled
            )
        }
    }

    override fun setWeekIndex(weekIndex: Int) {
        dataLiveData.updateValue(defaultValue = initialData) { oldValue ->
            oldValue.copy(
                weekIndex = weekIndex,
                lessons = getLessons(
                    weekIndex = weekIndex
                )
            )
        }
    }

    override fun disableFilter() {
        dataLiveData.updateValue(defaultValue = initialData) { oldValue ->
            oldValue.copy(
                filterEnabled = false,
                lessons = getLessons(
                    weekIndex = oldValue.weekIndex
                )
            )
        }
    }

    private fun getLessons(weekIndex: Int): List<GroupAndLesson> =
        lessonsInteractor.getAllLessons(weekIndex)
}