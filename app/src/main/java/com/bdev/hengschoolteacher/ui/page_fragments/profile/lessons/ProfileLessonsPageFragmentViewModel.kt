package com.bdev.hengschoolteacher.ui.page_fragments.profile.lessons

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.interactors.LessonStateService
import com.bdev.hengschoolteacher.interactors.LessonsAttendancesService
import com.bdev.hengschoolteacher.interactors.alerts.profile.AlertsProfileInteractor
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusStorageInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderInteractor
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.views.app.lesson.LessonRowViewData
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonsViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.RuntimeException
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
    private val lessonsStatusService: LessonsStatusStorageInteractor,
    private val lessonStateService: LessonStateService,
    private val profileInteractor: ProfileInteractor,
    private val alertsProfileService: AlertsProfileInteractor,
    private val staffMembersStorageInteractor: StaffMembersStorageInteractor,
    private val studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractor,
    private val lessonsAttendancesService: LessonsAttendancesService
): ProfileLessonsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    private val dataLiveData: MutableLiveDataWithState<ProfileLessonsPageFragmentData>

    init {
        dataLiveData = MutableLiveDataWithState(getInitialData())
    }

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun toggleFilter() {
        dataLiveData.updateValue { oldData ->
            val newFilterEnabled = !oldData.filterEnabled
            val newLessons = getLessonsData(weekIndex = oldData.weekIndex, filterEnabled = newFilterEnabled)
            val newNoLessons = getNoLessons(lessons = newLessons, filterEnabled = newFilterEnabled)

            oldData.copy(
                filterEnabled = newFilterEnabled,
                lessons = newLessons,
                noLessons = newNoLessons
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
            val newLessons = getLessonsData(weekIndex = weekIndex, filterEnabled = oldData.filterEnabled)
            val newNoLessons = getNoLessons(lessons = newLessons, filterEnabled = oldData.filterEnabled)

            oldData.copy(
                weekIndex = weekIndex,
                lessons = newLessons,
                noLessons = newNoLessons
            )
        }
    }

    override fun goBack() {
        navigate(navCommand = NavCommand.quit())
    }

    private fun getInitialData(): ProfileLessonsPageFragmentData {
        val me = profileInteractor.getMe() ?: throw RuntimeException()
        val lessons = getLessonsData(weekIndex = 0, filterEnabled = false)

        return ProfileLessonsPageFragmentData(
            me = me,
            hasLessonsAlert = alertsProfileService.haveLessonsAlerts(),
            hasDebtsAlert = alertsProfileService.haveDebtsAlerts(),
            hasPaymentsAlert = alertsProfileService.havePaymentsAlerts(),
            lessons = lessons,
            noLessons = getNoLessons(lessons = lessons, filterEnabled = false),
            weekIndex = 0,
            filterEnabled = true,
            calendarEnabled = false
        )
    }

    private fun getNoLessons(lessons: LessonsViewData, filterEnabled: Boolean): Boolean {
        return lessons.lessonsData.isEmpty() && filterEnabled
    }

    private fun getLessonsData(weekIndex: Int, filterEnabled: Boolean): LessonsViewData {
        return LessonsViewData(
            weekIndex = weekIndex,
            lessonsData = profileInteractor.getMe()?.let { me ->
                getLessonsRowsData(me = me, weekIndex = weekIndex, filterEnabled = filterEnabled)
            } ?: emptyList()
        )
    }

    private fun getLessonsRowsData(me: StaffMember, weekIndex: Int, filterEnabled: Boolean): List<LessonRowViewData> {
        return lessonsService
            .getTeacherLessons(teacherLogin = me.login, weekIndex = weekIndex)
            .filter {
                !filterEnabled || !lessonStateService.isLessonFilled(it.lesson, weekIndex)
            }
            .map { groupAndLesson ->
                LessonRowViewData(
                    staffMember = staffMembersStorageInteractor.getStaffMember(groupAndLesson.lesson.teacherLogin),
                    group = groupAndLesson.group,
                    lesson = groupAndLesson.lesson,
                    lessonStatus = lessonsStatusService.getLessonStatus(
                        lessonId = groupAndLesson.lesson.id,
                        lessonTime = lessonsService.getLessonStartTime(groupAndLesson.lesson.id, weekIndex)
                    ),
                    isLessonFinished = lessonStateService.isLessonFinished(
                        lessonId = groupAndLesson.lesson.id,
                        weekIndex = weekIndex
                    ),
                    isLessonAttendanceFilled = lessonsAttendancesService.isLessonAttendanceFilled(
                        lessonId = groupAndLesson.lesson.id,
                        weekIndex = weekIndex
                    ),
                    studentsToAttendanceType = lessonsService
                        .getLessonStudents(groupAndLesson.lesson.id, weekIndex)
                        .map { student ->
                            Pair(
                                student,
                                studentsAttendancesProviderInteractor.getAttendance(
                                    groupAndLesson.lesson.id,
                                    student.login,
                                    weekIndex
                                )
                            )
                        },
                    weekIndex = weekIndex,
                    showTeacher = false
                )
            }
    }
}