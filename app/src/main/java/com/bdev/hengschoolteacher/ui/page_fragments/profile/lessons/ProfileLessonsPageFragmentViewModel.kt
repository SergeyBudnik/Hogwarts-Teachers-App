package com.bdev.hengschoolteacher.ui.page_fragments.profile.lessons

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface ProfileLessonsPageFragmentViewModel : BasePageFragmentViewModel {
    fun getDataLiveData(): LiveData<ProfileLessonsPageFragmentData>

    fun toggleFilter()
    fun toggleCalendar()

    fun setWeekIndex(weekIndex: Int)
}

@HiltViewModel
class ProfileLessonsPageFragmentViewModelImpl @Inject constructor(
    private val lessonsService: LessonsInteractor,
    private val profileInteractor: ProfileInteractor,
): ProfileLessonsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    private val dataLiveData: MutableLiveDataWithState<ProfileLessonsPageFragmentData>

    init {
        dataLiveData = MutableLiveDataWithState(getInitialData())
    }

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun toggleFilter() {
        dataLiveData.updateValue { oldData ->
            oldData.copy(
                filterEnabled = !oldData.filterEnabled
            )
        }
    }

    override fun toggleCalendar() {
        dataLiveData.updateValue { oldData ->
            oldData.copy(calendarEnabled = !oldData.calendarEnabled)
        }
    }

    override fun setWeekIndex(weekIndex: Int) {
        dataLiveData.updateValue { oldData ->
            oldData.copy(
                weekIndex = weekIndex,
                lessons = getLessons(weekIndex = weekIndex),
            )
        }
    }

    override fun goBack() {
        navigate(navCommand = NavCommand.quit())
    }

    private fun getInitialData(): ProfileLessonsPageFragmentData {
        val lessons = getLessons(weekIndex = 0)

        return ProfileLessonsPageFragmentData(
            lessons = lessons,
            weekIndex = 0,
            filterEnabled = true,
            calendarEnabled = false
        )
    }

    private fun getLessons(weekIndex: Int): List<GroupAndLesson> =
        profileInteractor.getMe()?.let { me ->
            lessonsService.getTeacherLessons(
                teacherLogin = me.login,
                weekIndex = weekIndex
            )
        } ?: emptyList()
}