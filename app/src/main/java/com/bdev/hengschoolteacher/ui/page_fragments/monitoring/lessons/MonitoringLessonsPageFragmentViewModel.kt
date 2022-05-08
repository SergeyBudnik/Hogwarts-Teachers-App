package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.lessons

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringInteractor
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringLessonsPageFragmentViewModel : BasePageFragmentViewModel {
    fun getDataLiveData(): LiveData<MonitoringLessonsPageFragmentData>

    fun toggleFilter()
    fun toggleCalendar()

    fun setWeekIndex(weekIndex: Int)
}

@HiltViewModel
class MonitoringLessonsPageFragmentViewModelImpl @Inject constructor(
    private val lessonsService: LessonsInteractor
): MonitoringLessonsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    private val dataLiveData: MutableLiveDataWithState<MonitoringLessonsPageFragmentData>

    init {
        dataLiveData = MutableLiveDataWithState(
            initialValue = getInitialData()
        )
    }

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun goBack() {
        // todo
    }

    override fun toggleFilter() {
        dataLiveData.updateValue(defaultValue = getInitialData()) { oldValue ->
            oldValue.copy(
                filterEnabled = !oldValue.filterEnabled,
                lessons = getLessons(
                    weekIndex = oldValue.weekIndex
                )
            )
        }
    }

    override fun toggleCalendar() {
        dataLiveData.updateValue(defaultValue = getInitialData()) { oldValue ->
            oldValue.copy(
                calendarEnabled = !oldValue.calendarEnabled
            )
        }
    }

    override fun setWeekIndex(weekIndex: Int) {
        dataLiveData.updateValue(defaultValue = getInitialData()) { oldValue ->
            oldValue.copy(
                weekIndex = weekIndex,
                lessons = getLessons(
                    weekIndex = weekIndex
                )
            )
        }
    }

    private fun getInitialData(): MonitoringLessonsPageFragmentData =
        MonitoringLessonsPageFragmentData(
            filterEnabled = false,
            calendarEnabled = false,
            weekIndex = 0,
            lessons = getLessons(weekIndex = 0)
        )

    private fun getLessons(weekIndex: Int): List<GroupAndLesson> =
        lessonsService.getAllLessons(weekIndex)
}