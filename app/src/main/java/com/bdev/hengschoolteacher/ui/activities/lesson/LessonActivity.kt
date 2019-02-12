package com.bdev.hengschoolteacher.ui.activities.lesson

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.service.*
import com.bdev.hengschoolteacher.ui.activities.*
import com.bdev.hengschoolteacher.ui.activities.student.StudentPaymentActivity
import com.bdev.hengschoolteacher.ui.activities.student.StudentCallActivity_
import com.bdev.hengschoolteacher.ui.activities.student.StudentPaymentActivity_
import com.bdev.hengschoolteacher.ui.utils.RedirectUtils.Companion.redirect
import com.bdev.hengschoolteacher.ui.views.branded.BrandedButtonView
import kotlinx.android.synthetic.main.activity_lesson.*
import kotlinx.android.synthetic.main.view_item_lesson_student.view.*
import org.androidannotations.annotations.*

@EViewGroup(R.layout.view_item_lesson_student)
open class LessonStudentItemView : RelativeLayout {
    @Bean
    lateinit var studentsAttendanceService: StudentsAttendancesService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(student: Student, group: Group, lesson: Lesson): LessonStudentItemView {
        nameView.text = student.name

        bindAttendance(student, group, lesson)
        bindCall(student)
        bindPayment(student)

        return this
    }

    private fun bindPayment(student: Student) {
        paymentView.setOnClickListener {
            redirect(context as BaseActivity)
                    .to(StudentPaymentActivity_::class.java)
                    .withExtra(StudentPaymentActivity.EXTRA_STUDENT_ID, student.id)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }
    }

    private fun bindCall(student: Student) {
        callView.setOnClickListener {
            redirect(context as BaseActivity)
                    .to(StudentCallActivity_::class.java)
                    .withExtra(LessonStudentAttendanceActivity.EXTRA_STUDENT_ID, student.id)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }
    }

    private fun bindAttendance(student: Student, group: Group, lesson: Lesson) {
        val attendanceType = studentsAttendanceService.getAttendance(lesson.id, student.id)

        val colorId = when (attendanceType) {
            null -> R.color.fill_text_base
            StudentAttendance.Type.VISITED -> R.color.fill_text_positive
            StudentAttendance.Type.VALID_SKIP -> R.color.fill_text_warning
            StudentAttendance.Type.INVALID_SKIP -> R.color.fill_text_negative
        }

        attendanceView.setColorFilter(resources.getColor(colorId), PorterDuff.Mode.SRC_IN)

        attendanceView.setOnClickListener {
            redirect(context as BaseActivity)
                    .to(LessonStudentAttendanceActivity_::class.java)
                    .withExtra(LessonStudentAttendanceActivity.EXTRA_GROUP_ID, group.id)
                    .withExtra(LessonStudentAttendanceActivity.EXTRA_LESSON_ID, lesson.id)
                    .withExtra(LessonStudentAttendanceActivity.EXTRA_STUDENT_ID, student.id)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .goForResult(LessonActivity.REQUEST_CODE_LESSON_ATTENDANCE)
        }
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_lesson)
open class LessonActivity : BaseActivity() {
    companion object {
        const val REQUEST_CODE_LESSON_ATTENDANCE = 1
        const val REQUEST_CODE_LESSON_STATUS = 2

        const val EXTRA_GROUP_ID = "EXTRA_GROUP_ID"
        const val EXTRA_LESSON_ID = "EXTRA_LESSON_ID"
    }

    @Extra(EXTRA_GROUP_ID)
    @JvmField
    var groupId: Long = 0

    @Extra(EXTRA_LESSON_ID)
    @JvmField
    var lessonId: Long = 0

    @Bean
    lateinit var groupsService: GroupsService
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var lessonStatusService: LessonStatusService

    @AfterViews
    fun init() {
        lessonHeaderView.setLeftButtonAction { doFinish() }

        doInit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_LESSON_ATTENDANCE -> doInit()
                REQUEST_CODE_LESSON_STATUS -> doInit()
            }
        }
    }

    private fun doInit() {
        val group = groupsService.getGroup(groupId) ?: throw RuntimeException()
        val lesson = group.lessons.find { it.id == lessonId } ?: throw RuntimeException()
        val students = studentsService.getGroupStudents(groupId)
        val lessonStatus = lessonStatusService.getLessonStatus(lessonId, lessonsService.getLessonStartTime(lessonId))

        lessonTimeView.bind(lesson)
        lessonTeacherInfoView.bind(lesson.teacherId)

        if (lessonStatus != null) {
            lessonMarkStatusView.setText(when (lessonStatus.type) {
                LessonStatus.Type.FINISHED -> "Занятие проведено"
                LessonStatus.Type.CANCELED -> "Занятие отменено"
                LessonStatus.Type.MOVED -> "Занятие перенесено"
            })
            lessonMarkStatusView.setStyle(BrandedButtonView.Style.DEFAULT)
        }

        lessonMarkStatusView.setOnClickListener {
            redirect(this)
                    .to(LessonStatusActivity_::class.java)
                    .withExtra(LessonStatusActivity.EXTRA_LESSON_ID, lesson.id)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .goForResult(LessonActivity.REQUEST_CODE_LESSON_STATUS)
        }

        studentsContainerView.removeAllViews()

        students.forEach {
            studentsContainerView.addView(LessonStudentItemView_.build(this).bind(it, group, lesson))
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
}
