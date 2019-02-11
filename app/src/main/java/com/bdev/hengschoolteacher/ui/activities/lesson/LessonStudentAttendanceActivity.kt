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
        const val EXTRA_GROUP_ID = "EXTRA_GROUP_ID"
        const val EXTRA_LESSON_ID = "EXTRA_LESSON_ID"
        const val EXTRA_STUDENT_ID = "EXTRA_STUDENT_ID"
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

    @Extra(EXTRA_GROUP_ID)
    @JvmField
    var groupId: Long = 0

    @Extra(EXTRA_LESSON_ID)
    @JvmField
    var lessonId: Long = 0

    @Extra(EXTRA_STUDENT_ID)
    @JvmField
    var studentId: Long = 0

    @AfterViews
    fun init() {
        lessonAttendanceHeaderView.setLeftButtonAction { doFinish() }

        val lesson = lessonsService.getLesson(lessonId) ?: throw RuntimeException()
        val student = studentsService.getStudent(studentId) ?: throw RuntimeException()

        lessonStudentAttendanceLessonTimeView.bind(lesson)
        lessonStudentAttendanceStudentInfoView.bind(student)
        lessonStudentAttendanceTeacherInfoView.bind(lesson.teacherId)

        initButtons()
    }

    private fun initButtons() {
        val attendance = studentsAttendancesService.getAttendance(lessonId, studentId)

        initButton(
                studentAttendanceVisitButtonView,
                listOf(studentAttendanceVisitButtonView, studentAttendanceValidSkipButtonView, studentAttendanceInvalidSkipButtonView),
                attendance,
                StudentAttendance.Type.VISITED,
                "Посетил",
                R.color.success
        )

        initButton(
                studentAttendanceValidSkipButtonView,
                listOf(studentAttendanceVisitButtonView, studentAttendanceValidSkipButtonView, studentAttendanceInvalidSkipButtonView),
                attendance,
                StudentAttendance.Type.VALID_SKIP,
                "Уважительный пропуск",
                R.color.warning
        )

        initButton(
                studentAttendanceInvalidSkipButtonView,
                listOf(studentAttendanceVisitButtonView, studentAttendanceValidSkipButtonView, studentAttendanceInvalidSkipButtonView),
                attendance,
                StudentAttendance.Type.INVALID_SKIP,
                "Неуважительный пропуск",
                R.color.error
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
                    if (actualAttendance == buttonAttendance) { buttonColorId } else { R.color.faded }
            )
        }

        buttonView.setOnClickListener {
            allButtonsViews.forEach { btn ->
                btn.hideButtonIcon()
                btn.setOnClickListener { _ -> }
            }

            buttonView.setButtonInProgressIcon()

            studentsAttendancesAsyncService.addAttendance(StudentAttendance(
                    null,
                    studentId,
                    GroupType.GROUP, // ToDo,
                    studentsService.getGroupStudents(groupId).size,
                    lessonsService.getLessonStartTime(lessonId),
                    lessonsService.getLessonFinishTime(lessonId),
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
