package com.bdev.hengschoolteacher.ui.activities.lesson

import android.annotation.SuppressLint
import android.app.Activity
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.async.StudentsAttendancesAsyncService
import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.service.GroupsService
import com.bdev.hengschoolteacher.service.LessonsService
import com.bdev.hengschoolteacher.service.StudentsAttendancesService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.branded.BrandedActionButtonView
import kotlinx.android.synthetic.main.activity_lesson_student_attendance.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra

@SuppressLint("Registered")
@EActivity(R.layout.activity_lesson_student_attendance)
open class LessonStudentAttendanceActivity : BaseActivity() {
    companion object {
        const val EXTRA_LESSON_ID = "EXTRA_LESSON_ID"
        const val EXTRA_STUDENT_ID = "EXTRA_STUDENT_ID"
        const val EXTRA_WEEK_INDEX = "EXTRA_WEEK_INDEX"

        fun redirect(current: BaseActivity, lessonId: Long, studentId: Long, weekIndex: Int): RedirectBuilder {
            return RedirectBuilder
                    .redirect(current)
                    .to(LessonStudentAttendanceActivity_::class.java)
                    .withExtra(EXTRA_LESSON_ID, lessonId)
                    .withExtra(EXTRA_STUDENT_ID, studentId)
                    .withExtra(EXTRA_WEEK_INDEX, weekIndex)
        }
    }

    @Bean
    lateinit var groupsService: GroupsService
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var studentsAttendancesService: StudentsAttendancesService

    @Bean
    lateinit var studentsAttendancesAsyncService: StudentsAttendancesAsyncService

    @Extra(EXTRA_LESSON_ID)
    @JvmField
    var lessonId: Long = 0

    @Extra(EXTRA_STUDENT_ID)
    @JvmField
    var studentId: Long = 0

    @Extra(EXTRA_WEEK_INDEX)
    @JvmField
    var weekIndex: Int = 0

    @AfterViews
    fun init() {
        lessonAttendanceHeaderView.setLeftButtonAction { doFinish() }

        val groupAndLesson = lessonsService.getLesson(lessonId) ?: throw RuntimeException()

        val group = groupAndLesson.group
        val lesson = groupAndLesson.lesson
        val student = studentsService.getStudent(studentId) ?: throw RuntimeException()

        lessonStudentAttendanceLessonTimeView.bind(lesson, weekIndex)
        lessonStudentAttendanceStudentInfoView.bind(student)

        initButtons(group)
    }

    private fun initButtons(group: Group) {
        val attendance = studentsAttendancesService.getAttendance(lessonId, studentId, weekIndex)

        val allButtonsViews = listOf(
                studentAttendanceVisitButtonView,
                studentAttendanceValidSkipButtonView,
                studentAttendanceInvalidSkipButtonView
        )

        initButton(
                group = group,
                actualAttendance = attendance,
                buttonAttendance = StudentAttendance.Type.VISITED,
                currentButtonView = studentAttendanceVisitButtonView,
                allButtonsViews = allButtonsViews,
                text = "Посетил",
                buttonColorId = R.color.fill_text_basic_positive
        )

        initButton(
                group = group,
                actualAttendance = attendance,
                buttonAttendance = StudentAttendance.Type.VALID_SKIP,
                currentButtonView = studentAttendanceValidSkipButtonView,
                allButtonsViews = allButtonsViews,
                text = "Уважительный пропуск",
                buttonColorId = R.color.fill_text_basic_warning
        )

        initButton(
                group = group,
                actualAttendance = attendance,
                buttonAttendance = StudentAttendance.Type.INVALID_SKIP,
                currentButtonView = studentAttendanceInvalidSkipButtonView,
                allButtonsViews = allButtonsViews,
                text = "Неуважительный пропуск",
                buttonColorId = R.color.fill_text_basic_negative
        )
    }

    private fun initButton(
            group: Group,
            actualAttendance: StudentAttendance.Type?,
            buttonAttendance: StudentAttendance.Type,
            currentButtonView: BrandedActionButtonView,
            allButtonsViews: List<BrandedActionButtonView>,
            text: String,
            buttonColorId: Int
    ) {
        currentButtonView.setButtonText(text)

        if (actualAttendance != null) {
            currentButtonView.setButtonIcon(
                    R.drawable.ic_ok,
                    if (actualAttendance == buttonAttendance) { buttonColorId } else { R.color.fill_text_basic_light }
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

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        setResult(Activity.RESULT_OK)
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    private fun markButtonAttendance(group: Group, attendance: StudentAttendance.Type) {
        studentsAttendancesAsyncService
                .addAttendance(StudentAttendance(
                        null,
                        studentId,
                        group.type,
                        lessonsService.getLessonStudents(lessonId, weekIndex).size,
                        lessonsService.getLessonStartTime(lessonId, weekIndex),
                        lessonsService.getLessonFinishTime(lessonId, weekIndex),
                        attendance
                ))
                .onSuccess { runOnUiThread {
                    initButtons(group)
                    doFinish()
                } }
                .onAuthFail { /* ToDo */ }
                .onOtherFail { /* ToDo */ }
    }
}
