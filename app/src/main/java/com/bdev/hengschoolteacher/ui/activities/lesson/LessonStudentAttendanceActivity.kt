package com.bdev.hengschoolteacher.ui.activities.lesson

import android.annotation.SuppressLint
import android.app.Activity
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.async.StudentsAttendancesAsyncService
import com.bdev.hengschoolteacher.data.school.group.GroupType
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

        val lesson = lessonsService.getLesson(lessonId)?.lesson ?: throw RuntimeException()
        val student = studentsService.getStudent(studentId) ?: throw RuntimeException()

        lessonStudentAttendanceLessonTimeView.bind(lesson, weekIndex)
        lessonStudentAttendanceStudentInfoView.bind(student)

        initButtons()
    }

    private fun initButtons() {
        val attendance = studentsAttendancesService.getAttendance(lessonId, studentId, weekIndex)

        initButton(
                studentAttendanceVisitButtonView,
                listOf(studentAttendanceVisitButtonView, studentAttendanceValidSkipButtonView, studentAttendanceInvalidSkipButtonView),
                attendance,
                StudentAttendance.Type.VISITED,
                "Посетил",
                R.color.fill_text_basic_positive
        )

        initButton(
                studentAttendanceValidSkipButtonView,
                listOf(studentAttendanceVisitButtonView, studentAttendanceValidSkipButtonView, studentAttendanceInvalidSkipButtonView),
                attendance,
                StudentAttendance.Type.VALID_SKIP,
                "Уважительный пропуск",
                R.color.fill_text_basic_warning
        )

        initButton(
                studentAttendanceInvalidSkipButtonView,
                listOf(studentAttendanceVisitButtonView, studentAttendanceValidSkipButtonView, studentAttendanceInvalidSkipButtonView),
                attendance,
                StudentAttendance.Type.INVALID_SKIP,
                "Неуважительный пропуск",
                R.color.fill_text_basic_negative
        )
    }

    private fun initButton(
            buttonView: BrandedActionButtonView,
            allButtonsViews: List<BrandedActionButtonView>,
            actualAttendance: StudentAttendance.Type?,
            buttonAttendance: StudentAttendance.Type,
            text: String,
            buttonColorId: Int
    ) {
        buttonView.setButtonText(text)

        if (actualAttendance != null) {
            buttonView.setButtonIcon(
                    R.drawable.ic_ok,
                    if (actualAttendance == buttonAttendance) { buttonColorId } else { R.color.fill_text_basic_light }
            )
        }

        buttonView.setOnClickListener {
            allButtonsViews.forEach { btn ->
                btn.hideButtonIcon()
                btn.setOnClickListener { }
            }

            buttonView.setButtonInProgressIcon()

            studentsAttendancesAsyncService.addAttendance(StudentAttendance(
                    null,
                    studentId,
                    GroupType.GROUP, // ToDo,
                    lessonsService.getLessonStudents(lessonId, weekIndex).size,
                    lessonsService.getLessonStartTime(lessonId, weekIndex),
                    lessonsService.getLessonFinishTime(lessonId, weekIndex),
                    buttonAttendance
            ))
                    .onSuccess { runOnUiThread {
                        initButtons()
                        doFinish()
                    } }
                    .onAuthFail {  }
                    .onOtherFail {  }
        }
    }

    override fun onBackPressed() {
        doFinish()
    }

    fun doFinish() {
        setResult(Activity.RESULT_OK)
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
