package com.bdev.hengschoolteacher.ui.activities.lesson

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.service.*
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.student.*
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder.Companion.redirect
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

    fun bind(student: Student, lesson: Lesson, weekIndex: Int): LessonStudentItemView {
        lessonStudentItemNameView.text = student.name

        bindAttendance(student, lesson, weekIndex)
        bindPayment(student)

        setOnClickListener {
            StudentInformationActivity
                    .redirect(
                            current = context as BaseActivity,
                            studentId = student.id
                    )
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }

        return this
    }

    private fun bindPayment(student: Student) {
        lessonStudentItemPaymentView.setOnClickListener {
            StudentPaymentActivity
                    .redirect(
                            current = context as BaseActivity,
                            studentId = student.id
                    )
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }
    }

    private fun bindAttendance(student: Student, lesson: Lesson, weekIndex: Int) {
        val attendanceType = studentsAttendanceService.getAttendance(lesson.id, student.id, weekIndex)

        val colorId = when (attendanceType) {
            null -> R.color.fill_text_basic
            StudentAttendance.Type.VISITED -> R.color.fill_text_basic_positive
            StudentAttendance.Type.VALID_SKIP -> R.color.fill_text_basic_warning
            StudentAttendance.Type.INVALID_SKIP -> R.color.fill_text_basic_negative
        }

        lessonStudentItemAttendanceView.setColorFilter(resources.getColor(colorId), PorterDuff.Mode.SRC_IN)

        lessonStudentItemAttendanceView.setOnClickListener {
            LessonStudentAttendanceActivity
                    .redirect(
                            current = context as BaseActivity,
                            lessonId = lesson.id,
                            studentId = student.id,
                            weekIndex = weekIndex
                    )
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .goForResult(LessonActivity.REQUEST_CODE_LESSON_ATTENDANCE)
        }
    }
}

class LessonStudentsListAdapter(val lesson: Lesson, val weekIndex: Int, context: Context) : BaseItemsListAdapter<Student>(context) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView == null) {
            LessonStudentItemView_.build(context)
        } else {
            convertView as LessonStudentItemView
        }.bind(
                student = getItem(position),
                lesson = lesson,
                weekIndex = weekIndex
        )
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_lesson)
open class LessonActivity : BaseActivity() {
    companion object {
        const val REQUEST_CODE_LESSON_ATTENDANCE = 1
        const val REQUEST_CODE_LESSON_STATUS = 2
        const val REQUEST_CODE_LESSON_TRANSFER = 3

        const val EXTRA_GROUP_ID = "EXTRA_GROUP_ID"
        const val EXTRA_LESSON_ID = "EXTRA_LESSON_ID"
        const val EXTRA_WEEK_INDEX = "EXTRA_WEEK_INDEX"

        fun redirect(
                context: Context,
                groupId: Long,
                lessonId: Long,
                weekIndex: Int
        ): RedirectBuilder {
            return redirect(context as BaseActivity)
                    .to(LessonActivity_::class.java)
                    .withExtra(EXTRA_GROUP_ID, groupId)
                    .withExtra(EXTRA_LESSON_ID, lessonId)
                    .withExtra(EXTRA_WEEK_INDEX, weekIndex)
        }
    }

    @Extra(EXTRA_GROUP_ID)
    @JvmField
    var groupId: Long = 0

    @Extra(EXTRA_LESSON_ID)
    @JvmField
    var lessonId: Long = 0

    @Extra(EXTRA_WEEK_INDEX)
    @JvmField
    var weekIndex: Int = 0

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
        val students = lessonsService.getLessonStudents(lessonId, weekIndex)
        val lessonStatus = lessonStatusService.getLessonStatus(lessonId, lessonsService.getLessonStartTime(lessonId, weekIndex))

        lessonTimeView.bind(lesson, weekIndex)

        if (lessonStatus != null) {
            lessonMarkStatusView.setText(when (lessonStatus.type) {
                LessonStatus.Type.FINISHED -> "Занятие проведено"
                LessonStatus.Type.CANCELED -> "Занятие отменено"
                LessonStatus.Type.MOVED -> "Занятие перенесено"
            })
            lessonMarkStatusView.setStyle(BrandedButtonView.Style.DEFAULT)
        }

        lessonMarkStatusView.setOnClickListener {
            LessonStatusActivity
                    .redirect(
                            current = this,
                            lessonId = lessonId,
                            weekIndex = weekIndex
                    )
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .goForResult(LessonActivity.REQUEST_CODE_LESSON_STATUS)
        }

        lessonAddTransferView.setOnClickListener {
            LessonTransferActivity
                    .redirect(
                            context = this,
                            groupId = groupId,
                            lessonId = lessonId,
                            weekIndex = weekIndex
                    )
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .goForResult(LessonActivity.REQUEST_CODE_LESSON_TRANSFER)
        }

        val adapter = LessonStudentsListAdapter(lesson, weekIndex, this)

        adapter.setItems(students)

        lessonStudentsListView.adapter = adapter
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
