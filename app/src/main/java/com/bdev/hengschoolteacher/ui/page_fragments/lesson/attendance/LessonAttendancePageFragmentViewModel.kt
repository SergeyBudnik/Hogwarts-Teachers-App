package com.bdev.hengschoolteacher.ui.page_fragments.lesson.attendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.exceptions.app.AppIncorrectStateException
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesModifierInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderInteractor
import com.bdev.hengschoolteacher.interactors.teachers.TeacherInfoInteractor
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface LessonAttendancePageFragmentViewModel : BasePageFragmentViewModel {
    fun getDataLiveData(): LiveData<LessonAttendancePageFragmentData>

    fun markAttendance(attendanceType: StudentAttendanceType)
}

@HiltViewModel
class LessonAttendancePageFragmentViewModelImpl @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val studentsStorageInteractor: StudentsStorageInteractor,
    private val lessonsInteractor: LessonsInteractor,
    private val studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractor,
    private val studentsAttendancesModifierInteractor: StudentsAttendancesModifierInteractor,
    private val staffMembersStorageInteractor: StaffMembersStorageInteractor,
    private val teacherInfoInteractor: TeacherInfoInteractor
): LessonAttendancePageFragmentViewModel, BasePageFragmentViewModelImpl() {
    private val dataLiveData =
        savedStateHandle.get<LessonAttendancePageFragmentArguments>("args")?.let { args ->
            val groupAndLesson = lessonsInteractor.getLesson(
                lessonId = args.lessonId
            ) ?: throw AppIncorrectStateException("Lesson attendance - missing lesson by id ${args.lessonId}")

            val student = studentsStorageInteractor.getByLogin(
                studentLogin = args.studentLogin
            ) ?: throw AppIncorrectStateException("Lesson attendance - missing student by login ${args.studentLogin}")

            val group = groupAndLesson.group
            val lesson = groupAndLesson.lesson

            val teacher = staffMembersStorageInteractor.getStaffMember(lesson.teacherLogin)

            MutableLiveDataWithState(
                initialValue = LessonAttendancePageFragmentData(
                    lesson = lesson,
                    group = group,
                    student = student,
                    attendance = studentsAttendancesProviderInteractor.getAttendance(
                        lessonId = args.lessonId,
                        studentLogin = args.studentLogin,
                        weekIndex = args.weekIndex
                    ),
                    progressAttendance = null,
                    lessonStartTime = lessonsInteractor.getLessonStartTime(lesson.id, args.weekIndex),
                    lessonFinishTime = lessonsInteractor.getLessonFinishTime(lesson.id, args.weekIndex),
                    lessonStudentsAmount = lessonsInteractor.getLessonStudents(lesson.id, args.weekIndex).size,
                    teacherName = teacher?.let { teacherInfoInteractor.getTeachersName(teacher) } ?: "",
                    teacherSurname = teacher?.let { teacherInfoInteractor.getTeachersSurname(teacher) } ?: ""
                )
            )
        } ?: throw AppIncorrectStateException("Lesson attendance - missing arguments")

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun markAttendance(attendanceType: StudentAttendanceType) {
        val value = dataLiveData.getValue()

        dataLiveData.setValue(mutator = { oldValue ->
            oldValue.copy(progressAttendance = attendanceType)
        })

        studentsAttendancesModifierInteractor.addAttendance(
            attendance = StudentAttendance(
                studentLogin = value.student.login,
                groupType = value.group.type,
                studentsInGroup = value.lessonStudentsAmount,
                startTime = value.lessonStartTime,
                finishTime = value.lessonFinishTime,
                type = attendanceType,
                ignoreSingleStudentPricing = value.lesson.ignoreSingleStudentPricing
            )
        ).onSuccess {
            dataLiveData.postValue(mutator = { oldValue ->
                oldValue.copy(
                    attendance = attendanceType,
                    progressAttendance = null
                )
            })

            navigate(navCommand = NavCommand.back())
        }.onAuthFail {
            /* ToDo */
        }.onOtherFail {
            /* ToDo */
        }
    }

    override fun goBack() {
        navigate(navCommand = NavCommand.back())
    }
}