package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.lessons

import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.data.common.NullableMutableLiveDataWithState
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.data.MonitoringPageFragmentTab
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.MonitoringContentFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.MonitoringContentFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringLessonsFragmentViewModel : MonitoringContentFragmentViewModel<MonitoringLessonsFragmentData> {
    fun setWeekIndex(weekIndex: Int)

    fun disableFilter()
}

@HiltViewModel
class MonitoringLessonsFragmentViewModelImpl @Inject constructor(
    private val lessonsInteractor: LessonsInteractor
): MonitoringLessonsFragmentViewModel, MonitoringContentFragmentViewModelImpl<MonitoringLessonsFragmentData>() {
    private val dataLiveData = MutableLiveDataWithState(initialValue = MonitoringLessonsFragmentData(
        visible = true,
        filterEnabled = false,
        calendarEnabled = false,
        weekIndex = 0,
        lessons = getLessons(weekIndex = 0)
    ))

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun isVisible() = dataLiveData.getValue().visible

    override fun setCurrentTab(tab: MonitoringPageFragmentTab) {
        dataLiveData.setValue(mutator = { oldValue ->
            oldValue.copy(visible = (tab == MonitoringPageFragmentTab.LESSONS))
        })
    }

    override fun onHeaderButton1Clicked() {
        super.onHeaderButton1Clicked()

        dataLiveData.setValue(mutator = { oldValue ->
            oldValue.copy(
                filterEnabled = !oldValue.filterEnabled,
                lessons = getLessons(
                    weekIndex = oldValue.weekIndex
                )
            )
        })
    }

    override fun onHeaderButton2Clicked() {
        super.onHeaderButton2Clicked()

        dataLiveData.setValue(mutator = { oldValue ->
            oldValue.copy(
                calendarEnabled = !oldValue.calendarEnabled
            )
        })
    }

    override fun setWeekIndex(weekIndex: Int) {
        dataLiveData.setValue(mutator = { oldValue ->
            oldValue.copy(
                weekIndex = weekIndex,
                lessons = getLessons(
                    weekIndex = weekIndex
                )
            )
        })
    }

    override fun disableFilter() {
        dataLiveData.setValue(mutator = { oldValue ->
            oldValue.copy(
                filterEnabled = false,
                lessons = getLessons(
                    weekIndex = oldValue.weekIndex
                )
            )
        })
    }

    private fun getLessons(weekIndex: Int): List<GroupAndLesson> =
        lessonsInteractor.getAllLessons(weekIndex)
}