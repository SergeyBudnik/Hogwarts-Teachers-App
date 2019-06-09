package com.bdev.hengschoolteacher.ui.activities.student

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.service.LessonsService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.activities.*
import com.bdev.hengschoolteacher.ui.activities.lesson.LessonStudentAttendanceActivity
import com.bdev.hengschoolteacher.ui.adapters.BaseWeekItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder.Companion.redirect
import kotlinx.android.synthetic.main.activity_student_information.*
import kotlinx.android.synthetic.main.view_list_item_student_information_timetable.view.*
import org.androidannotations.annotations.*
import java.util.*

@EViewGroup(R.layout.view_list_item_student_information_timetable)
open class StudentInformationTimetableListItemView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(groupAndLesson: GroupAndLesson): StudentInformationTimetableListItemView {
        val startTimeName = resources.getString(groupAndLesson.lesson.startTime.translationId)
        val finishTimeName = resources.getString(groupAndLesson.lesson.finishTime.translationId)

        studentInformationListItemTimeView.text = "$startTimeName - $finishTimeName"

        return this
    }
}

class StudentInformationTimetableListAdapter(context: Context) : BaseWeekItemsListAdapter<GroupAndLesson>(context) {
    override fun getElementView(item: GroupAndLesson, convertView: View?): View {
        return if (convertView == null || convertView !is StudentInformationTimetableListItemView) {
            StudentInformationTimetableListItemView_.build(context)
        } else {
            convertView
        }.bind(item)
    }

    override fun getElementDayOfWeek(item: GroupAndLesson): DayOfWeek {
        return item.lesson.day
    }

    override fun getElementComparator(): Comparator<GroupAndLesson> {
        return GroupAndLesson.getComparator(Calendar.getInstance())
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_student_information)
open class StudentInformationActivity : BaseActivity() {
    companion object {
        const val EXTRA_STUDENT_ID = "EXTRA_STUDENT_ID"

        fun redirect(current: BaseActivity, studentId: Long): RedirectBuilder {
            return RedirectBuilder
                    .redirect(current)
                    .to(StudentInformationActivity_::class.java)
                    .withExtra(EXTRA_STUDENT_ID, studentId)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
        }
    }

    @Extra(EXTRA_STUDENT_ID)
    @JvmField
    var studentId = 0L

    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var lessonsService: LessonsService

    private val adapter = StudentInformationTimetableListAdapter(this)

    @AfterViews
    fun init() {
        studentInformationHeaderView.setLeftButtonAction { doFinish() }

        val student = studentsService.getStudent(studentId) ?: throw RuntimeException()

        studentInformationSecondaryHeaderView.bind(student)

        studentNameView.text = student.name

        studentInformationCallView.setOnClickListener {
            redirect(this)
                    .to(StudentCallActivity_::class.java)
                    .withExtra(LessonStudentAttendanceActivity.EXTRA_STUDENT_ID, student.id)
                    .withExtra(LessonStudentAttendanceActivity.EXTRA_WEEK_INDEX, 0) // WeekIndex
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }

        studentInformationPaymentView.setOnClickListener {
            StudentPaymentActivity
                    .redirect(
                            current = this,
                            studentId = student.id
                    )
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }

        adapter.setItems(lessonsService.getStudentLessons(student.id))

        studentInformationTimetableListView.adapter = adapter
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
