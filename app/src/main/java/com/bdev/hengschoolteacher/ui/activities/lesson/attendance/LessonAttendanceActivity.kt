package com.bdev.hengschoolteacher.ui.activities.lesson.attendance

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.services.groups.GroupsStorageService
import com.bdev.hengschoolteacher.services.lessons.LessonsService
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.groups.GroupsStorageServiceImpl
import com.bdev.hengschoolteacher.services.staff.StaffMembersStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.services.students_attendances.StudentsAttendancesModifierService
import com.bdev.hengschoolteacher.services.students_attendances.StudentsAttendancesProviderService
import com.bdev.hengschoolteacher.services.teacher.TeacherInfoService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.lesson.attendance.LessonAttendanceActivityParams.EXTRA_DATA
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.branded.BrandedActionButtonView
import kotlinx.android.synthetic.main.activity_lesson_student_attendance.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra

@SuppressLint("Registered")
@EActivity(R.layout.activity_lesson_student_attendance)
open class LessonAttendanceActivity : BaseActivity() {
    @Bean(GroupsStorageServiceImpl::class)
    lateinit var groupsStorageService: GroupsStorageService
    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderService
    @Bean
    lateinit var studentsAttendancesModifierService: StudentsAttendancesModifierService
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageService
    @Bean
    lateinit var teacherInfoService: TeacherInfoService

    @Extra(EXTRA_DATA)
    lateinit var activityData: LessonAttendanceActivityData

    @AfterViews
    fun init() {
        lessonAttendanceHeaderView.setLeftButtonAction { doFinish() }

        val groupAndLesson = lessonsService.getLesson(activityData.lessonId) ?: throw RuntimeException()

        val group = groupAndLesson.group
        val lesson = groupAndLesson.lesson
        val student = studentsStorageService.getByLogin(activityData.studentLogin) ?: throw RuntimeException()

        staffMembersStorageService.getStaffMember(lesson.teacherLogin)?.let { teacher ->
            lessonStudentAttendanceLessonTimeView.bind(
                    lesson = lesson,
                    lessonStartTime = lessonsService.getLessonStartTime(lesson.id, activityData.weekIndex),
                    teacherName = teacherInfoService.getTeachersName(teacher),
                    teacherSurname = teacherInfoService.getTeachersSurname(teacher)
            )
        }

        lessonStudentAttendanceStudentInfoView.bind(student)

        initButtons(group)
    }

    private fun initButtons(group: Group) {
        val attendance = studentsAttendancesProviderService.getAttendance(
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

    override fun onBackPressed() = LessonAttendanceActivityNavigation.goBackWithCancel(from = this)

    private fun doFinish() = LessonAttendanceActivityNavigation.goBackWithSuccess(from = this)

    private fun markButtonAttendance(group: Group, attendance: StudentAttendanceType) {
        val lesson = lessonsService.getLesson(lessonId = activityData.lessonId)

        studentsAttendancesModifierService
                .addAttendance(StudentAttendance(
                        studentLogin = activityData.studentLogin,
                        groupType = group.type,
                        studentsInGroup = lessonsService.getLessonStudents(activityData.lessonId, activityData.weekIndex).size,
                        startTime = lessonsService.getLessonStartTime(activityData.lessonId, activityData.weekIndex),
                        finishTime = lessonsService.getLessonFinishTime(activityData.lessonId, activityData.weekIndex),
                        type = attendance,
                        ignoreSingleStudentPricing = lesson?.lesson?.ignoreSingleStudentPricing ?: false
                ))
                .onSuccess { runOnUiThread {
                    initButtons(group)
                    doFinish()
                } }
                .onAuthFail { /* ToDo */ }
                .onOtherFail { /* ToDo */ }
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
