package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.lessons

import com.bdev.hengschoolteacher.data.common.NullableMutableLiveDataWithState
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab
import com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.ProfileContentFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.ProfileContentFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface ProfileLessonsPageFragmentViewModel : ProfileContentFragmentViewModel<ProfileLessonsFragmentData> {
    fun setWeekIndex(weekIndex: Int)

    fun disableFilter()
}

@HiltViewModel
class ProfileLessonsPageFragmentViewModelImpl @Inject constructor(
    private val lessonsService: LessonsInteractor,
    private val profileInteractor: ProfileInteractor,
): ProfileLessonsPageFragmentViewModel, ProfileContentFragmentViewModelImpl<ProfileLessonsFragmentData>() {
    private val initialData = ProfileLessonsFragmentData(
        visible = false,
        lessons = getLessons(weekIndex = 0),
        weekIndex = 0,
        filterEnabled = true,
        calendarEnabled = false
    )

    private val dataLiveData = NullableMutableLiveDataWithState(
        initialValue = initialData
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun isVisible() = dataLiveData.getValue()?.visible ?: false

    override fun setCurrentTab(tab: ProfilePageFragmentTab) {
        dataLiveData.updateValue(defaultValue = initialData) { oldData ->
            oldData.copy(visible = (tab == ProfilePageFragmentTab.LESSONS))
        }
    }

    override fun setWeekIndex(weekIndex: Int) {
        dataLiveData.updateValue(defaultValue = initialData) { oldData ->
            oldData.copy(
                weekIndex = weekIndex,
                lessons = getLessons(weekIndex = weekIndex),
            )
        }
    }

    override fun disableFilter() {
        dataLiveData.updateValue(defaultValue = initialData) { oldData ->
            oldData.copy(
                filterEnabled = false
            )
        }
    }

    override fun onHeaderButton1Clicked() {
        dataLiveData.updateValue(defaultValue = initialData) { oldData ->
            oldData.copy(
                filterEnabled = !oldData.filterEnabled
            )
        }
    }

    override fun onHeaderButton2Clicked() {
        dataLiveData.updateValue(defaultValue = initialData) { oldData ->
            oldData.copy(calendarEnabled = !oldData.calendarEnabled)
        }
    }

    private fun getLessons(weekIndex: Int): List<GroupAndLesson> =
        profileInteractor.getMe()?.let { me ->
            lessonsService.getTeacherLessons(
                teacherLogin = me.login,
                weekIndex = weekIndex
            )
        } ?: emptyList()
}