package com.bdev.hengschoolteacher.ui.page_fragments.lesson.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.interactors.groups.GroupsStorageInteractor
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesModifierInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderInteractor
import com.bdev.hengschoolteacher.interactors.teachers.TeacherInfoInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.branded.BrandedActionButtonView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_lesson_student_attendance.*
import javax.inject.Inject

@AndroidEntryPoint
class LessonAttendancePageFragment : BasePageFragment<LessonAttendancePageFragmentViewModel>() {
    @Inject lateinit var groupsStorageInteractor: GroupsStorageInteractor
    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var lessonsService: LessonsInteractor
    @Inject lateinit var studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractor
    @Inject lateinit var studentsAttendancesModifierInteractor: StudentsAttendancesModifierInteractor
    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor
    @Inject lateinit var teacherInfoInteractor: TeacherInfoInteractor

    lateinit var activityData: LessonAttendanceActivityData

    override fun provideViewModel(): LessonAttendancePageFragmentViewModel =
        ViewModelProvider(this).get(LessonAttendancePageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_lesson_student_attendance, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        // activityData = intent.getSerializableExtra(EXTRA_DATA) as LessonAttendanceActivityData todo

        lessonAttendanceHeaderView.setLeftButtonAction { doFinish() }

        val groupAndLesson = lessonsService.getLesson(activityData.lessonId) ?: throw RuntimeException()

        val group = groupAndLesson.group
        val lesson = groupAndLesson.lesson
        val student = studentsStorageInteractor.getByLogin(activityData.studentLogin) ?: throw RuntimeException()

        staffMembersStorageInteractor.getStaffMember(lesson.teacherLogin)?.let { teacher ->
            lessonStudentAttendanceLessonTimeView.bind(
                    lesson = lesson,
                    lessonStartTime = lessonsService.getLessonStartTime(lesson.id, activityData.weekIndex),
                    teacherName = teacherInfoInteractor.getTeachersName(teacher),
                    teacherSurname = teacherInfoInteractor.getTeachersSurname(teacher)
            )
        }

        lessonStudentAttendanceStudentInfoView.bind(student)

        initButtons(group)
    }

    private fun initButtons(group: Group) {
        val attendance = studentsAttendancesProviderInteractor.getAttendance(
                lessonId = activityData.lessonId,
                studentLogin = activityData.studentLogin,
                weekIndex = activityData.weekIndex
        )

        val allButtonsViews = listOf(
                studentAttendanceVisitButtonView,
                studentAttendanceValidSkipButtonView,
                studentAttendanceInvalidSkipButtonView
        )

        initButton(
                group = group,
                actualAttendance = attendance,
                buttonAttendance = StudentAttendanceType.VISITED,
                currentButtonView = studentAttendanceVisitButtonView,
                allButtonsViews = allButtonsViews,
                text = "Посетил",
                buttonColorId = R.color.fill_text_basic_positive
        )

        initButton(
                group = group,
                actualAttendance = attendance,
                buttonAttendance = StudentAttendanceType.VALID_SKIP,
                currentButtonView = studentAttendanceValidSkipButtonView,
                allButtonsViews = allButtonsViews,
                text = "Уважительный пропуск",
                buttonColorId = R.color.fill_text_basic_warning
        )

        initButton(
                group = group,
                actualAttendance = attendance,
                buttonAttendance = StudentAttendanceType.INVALID_SKIP,
                currentButtonView = studentAttendanceInvalidSkipButtonView,
                allButtonsViews = allButtonsViews,
                text = "Неуважительный пропуск",
                buttonColorId = R.color.fill_text_basic_negative
        )

        initButton(
                group = group,
                actualAttendance = attendance,
                buttonAttendance = StudentAttendanceType.FREE_LESSON,
                currentButtonView = studentAttendanceFreeLessonButtonView,
                allButtonsViews = allButtonsViews,
                text = "Бесплатное занятие",
                buttonColorId = R.color.fill_text_basic_action_link
        )
    }

    private fun initButton(
            group: Group,
            actualAttendance: StudentAttendanceType?,
            buttonAttendance: StudentAttendanceType,
            currentButtonView: BrandedActionButtonView,
            allButtonsViews: List<BrandedActionButtonView>,
            text: String,
            buttonColorId: Int
    ) {
        currentButtonView.setButtonText(text)

        if (actualAttendance != null) {
            currentButtonView.setButtonIcon(
                    R.drawable.ic_ok,
                    if (actualAttendance == buttonAttendance) {
                        buttonColorId
                    } else {
                        R.color.fill_text_basic_light
                    }
            )
        }

        currentButtonView.setOnClickListener {
            allButtonsViews.forEach { btn ->
                btn.hideButtonIcon()
            }

            currentButtonView.setButtonInProgressIcon()

            markButtonAttendance(group = group, attendance = buttonAttendance)
        }
    }

    private fun doFinish() = LessonAttendanceActivityNavigation.goBackWithSuccess(from = this)

    private fun markButtonAttendance(group: Group, attendance: StudentAttendanceType) {
        val lesson = lessonsService.getLesson(lessonId = activityData.lessonId)

        studentsAttendancesModifierInteractor
                .addAttendance(StudentAttendance(
                        studentLogin = activityData.studentLogin,
                        groupType = group.type,
                        studentsInGroup = lessonsService.getLessonStudents(activityData.lessonId, activityData.weekIndex).size,
                        startTime = lessonsService.getLessonStartTime(activityData.lessonId, activityData.weekIndex),
                        finishTime = lessonsService.getLessonFinishTime(activityData.lessonId, activityData.weekIndex),
                        type = attendance,
                        ignoreSingleStudentPricing = lesson?.lesson?.ignoreSingleStudentPricing ?: false
                ))
                .onSuccess {
                    initButtons(group)
                    doFinish()
                }
                .onAuthFail { /* ToDo */ }
                .onOtherFail { /* ToDo */ }
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
