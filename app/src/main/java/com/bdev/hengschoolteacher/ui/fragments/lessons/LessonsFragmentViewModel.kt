package com.bdev.hengschoolteacher.ui.fragments.lessons

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.NavGraphDirections
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.interactors.LessonStateService
import com.bdev.hengschoolteacher.interactors.LessonsAttendancesService
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusStorageInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderInteractor
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModel
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.lesson.info.LessonInfoPageFragmentArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface LessonsFragmentViewModel : BaseFragmentViewModel {
    fun getDataLiveData(): LiveData<LessonsFragmentData>

    fun update(lessons: List<GroupAndLesson>, weekIndex: Int, filterEnabled: Boolean)

    fun navigateToLesson(lessonRowViewData: LessonsFragmentItemData)
}

@HiltViewModel
class LessonsFragmentViewModelImpl @Inject constructor(
    private val lessonsService: LessonsInteractor,
    private val lessonStateService: LessonStateService,
    private val staffMembersStorageInteractor: StaffMembersStorageInteractor,
    private val studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractor,
    private val lessonsAttendancesService: LessonsAttendancesService,
    private val lessonsStatusService: LessonsStatusStorageInteractor
): LessonsFragmentViewModel, BaseFragmentViewModelImpl() {
    private val dataLiveData = MutableLiveDataWithState<LessonsFragmentData>(
        initialValue = null
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun update(lessons: List<GroupAndLesson>, weekIndex: Int, filterEnabled: Boolean) {
        dataLiveData.updateValue {
            LessonsFragmentData(
                lessons = getLessonsViewData(
                    lessons = lessons,
                    weekIndex = weekIndex,
                    filterEnabled = filterEnabled
                ),
                weekIndex = weekIndex,
                filterEnabled = filterEnabled
            )
        }
    }

    override fun navigateToLesson(lessonRowViewData: LessonsFragmentItemData) {
        navigate(
            navCommand = NavCommand.forward(
                navDir = NavGraphDirections.toLessonInfoAction(
                    args = LessonInfoPageFragmentArguments(
                        groupId = lessonRowViewData.group.id,
                        lessonId = lessonRowViewData.lesson.id,
                        weekIndex = lessonRowViewData.weekIndex
                    )
                )
            )
        )
    }

    private fun getLessonsViewData(lessons: List<GroupAndLesson>, weekIndex: Int, filterEnabled: Boolean): List<LessonsFragmentItemData> =
        lessons.filter {
            !filterEnabled || !lessonStateService.isLessonFilled(it.lesson, weekIndex)
        }.map { groupAndLesson ->
            LessonsFragmentItemData(
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
                showTeacher = true
            )
        }
}