package com.bdev.hengschoolteacher.ui.page_fragments.lesson.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.bdev.hengschoolteacher.NavGraphDirections
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.interactors.groups.GroupsStorageInteractor
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusStorageInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderInteractor
import com.bdev.hengschoolteacher.interactors.students_debts.StudentsDebtsInteractor
import com.bdev.hengschoolteacher.interactors.teachers.TeacherInfoInteractor
import com.bdev.hengschoolteacher.ui.fragments.BaseFragment
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.lesson.attendance.LessonAttendancePageFragmentArguments
import com.bdev.hengschoolteacher.ui.page_fragments.lesson.info.data.LessonInfoStudent
import com.bdev.hengschoolteacher.ui.page_fragments.lesson.status.LessonStatusPageFragmentArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface LessonInfoPageFragmentViewModel : BasePageFragmentViewModel {
    fun getDataLiveData(): LiveData<LessonInfoPageFragmentData>

    fun goToLessonStatus()
    fun goToLessonAttendance(studentLogin: String)

    fun goToStudentInformation(studentLogin: String)
    fun goToStudentPayments(studentLogin: String)
}

@HiltViewModel
class LessonInfoPageFragmentViewModelImpl @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val groupsStorageInteractor: GroupsStorageInteractor,
    private val lessonsService: LessonsInteractor,
    private val lessonsStatusService: LessonsStatusStorageInteractor,
    private val studentsDebtsInteractor: StudentsDebtsInteractor,
    private val studentsAttendanceProviderInteractor: StudentsAttendancesProviderInteractor,
    private val staffMembersStorageInteractor: StaffMembersStorageInteractor,
    private val teacherInfoInteractor: TeacherInfoInteractor
) : LessonInfoPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    private val dataLiveData =
        savedStateHandle.get<LessonInfoPageFragmentArguments>("args")?.let { args ->
            val group = groupsStorageInteractor.getById(args.groupId) ?: throw RuntimeException()
            val lesson = group.lessons.find { it.id == args.lessonId } ?: throw RuntimeException()
            val students = lessonsService.getLessonStudents(args.lessonId, args.weekIndex)
            val lessonStatus = lessonsStatusService.getLessonStatus(
                args.lessonId,
                lessonsService.getLessonStartTime(
                    args.lessonId,
                    args.weekIndex
                )
            )

            val teacher = staffMembersStorageInteractor.getStaffMember(lesson.teacherLogin) ?: throw RuntimeException()

            MutableLiveDataWithState(
                initialValue = LessonInfoPageFragmentData(
                    group = group,
                    lesson = lesson,
                    lessonStartTime = lessonsService.getLessonStartTime(lesson.id, args.weekIndex),
                    students = students.map { student ->
                        LessonInfoStudent(
                            login = student.login,
                            name = student.person.name,
                            attendanceType = studentsAttendanceProviderInteractor.getAttendance(lesson.id, student.login, args.weekIndex),
                            currentDebt = studentsDebtsInteractor.getDebt(student.login),
                            expectedDebt = studentsDebtsInteractor.getExpectedDebt(student.login)
                        )
                    },
                    lessonStatus = lessonStatus,
                    teacherName = teacherInfoInteractor.getTeachersName(teacher),
                    teacherSurname = teacherInfoInteractor.getTeachersSurname(teacher),
                    weekIndex = args.weekIndex
                )
            )
        } ?: throw RuntimeException()

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun goToLessonStatus() {
        dataLiveData.getValue().let { value ->
            navigate(
                navCommand = NavCommand.forward(
                    navDir = NavGraphDirections.toLessonStatusAction(
                        args = LessonStatusPageFragmentArguments(
                            lessonId = value.lesson.id,
                            weekIndex = value.weekIndex
                        )
                    )
                )
            )
        }
    }

    override fun goToLessonAttendance(studentLogin: String) {
        dataLiveData.getValue().let { value ->
            navigate(
                navCommand = NavCommand.forward(
                    navDir = NavGraphDirections.toLessonAttendanceAction(
                        args = LessonAttendancePageFragmentArguments(
                            lessonId = value.lesson.id,
                            weekIndex = value.weekIndex,
                            studentLogin = studentLogin
                        )
                    )
                )
            )
        }
    }

    override fun goToStudentInformation(studentLogin: String) {
        // TODO
    }

    override fun goToStudentPayments(studentLogin: String) {
        // TODO
    }

    override fun onResume(fragment: BaseFragment<*>) {
        super.onResume(fragment)

        dataLiveData.setValue(mutator = { oldValue ->
            val students = lessonsService.getLessonStudents(oldValue.lesson.id, oldValue.weekIndex)

            val lessonStatus = lessonsStatusService.getLessonStatus(
                oldValue.lesson.id,
                lessonsService.getLessonStartTime(
                    oldValue.lesson.id,
                    oldValue.weekIndex
                )
            )

            oldValue.copy(
                students = students.map { student ->
                    LessonInfoStudent(
                        login = student.login,
                        name = student.person.name,
                        attendanceType = studentsAttendanceProviderInteractor.getAttendance(oldValue.lesson.id, student.login, oldValue.weekIndex),
                        currentDebt = studentsDebtsInteractor.getDebt(student.login),
                        expectedDebt = studentsDebtsInteractor.getExpectedDebt(student.login)
                    )
                },
                lessonStatus = lessonStatus,
            )
        })
    }

    override fun goBack() { doGoBack() }

    private fun doGoBack() { navigate(navCommand = NavCommand.back()) }
}