package com.bdev.hengschoolteacher.ui.activities.monitoring

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.Month
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.service.LessonsService
import com.bdev.hengschoolteacher.service.StudentsAttendancesService
import com.bdev.hengschoolteacher.service.StudentsPaymentsService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.utils.TimeUtils
import kotlinx.android.synthetic.main.activity_monitoring_student_payment.*
import kotlinx.android.synthetic.main.view_monitoring_student_payment_item.view.*
import org.androidannotations.annotations.*
import java.lang.RuntimeException

@EViewGroup(R.layout.view_monitoring_student_payment_item)
open class MonitoringStudentPaymentItemView : RelativeLayout {
    @Bean
    lateinit var lessonsService: LessonsService

    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(
            student: Student,
            month: Int,
            allVisitedAttendances: List<StudentAttendance>,
            allValidSkipAttendances: List<StudentAttendance>,
            allInvalidSkipAttendances: List<StudentAttendance>
    ) {
        val visitedLessonsAmount = getLessonsAmount(allVisitedAttendances, month)
        val validSkipLessonsAmount = getLessonsAmount(allValidSkipAttendances, month)
        val invalidSkipLessonsAmount = getLessonsAmount(allInvalidSkipAttendances, month)

        monitoringStudentPaymentItemMonthView.text = resources.getString(Month.findByIndex(month).nameId)

        monitoringStudentPaymentItemVisitedLessonsAmountView.text = "$visitedLessonsAmount"
        monitoringStudentPaymentItemValidSkipLessonsAmountView.text = "$validSkipLessonsAmount"
        monitoringStudentPaymentItemInvalidSkipLessonsAmountView.text = "$invalidSkipLessonsAmount"
    }

    private fun getLessonsAmount(attendances: List<StudentAttendance>, month: Int): Int {
        val startTime = TimeUtils().getMonthStart(month)
        val finishTime = TimeUtils().getMonthFinish(month)

        return attendances
                .asSequence()
                .filter { it.startTime <= finishTime }
                .filter { it.startTime >= startTime }
                .count()
    }

    private fun getTotalLessonsAmount(student: Student, month: Int): Int {
        return lessonsService.getLessonsAmountInMonth(student.studentGroups[0].groupId, month)
    }
}

@EBean
open class MonitoringStudentPaymentListAdapter : BaseAdapter() {
    @RootContext
    lateinit var context: Context

    private lateinit var student: Student
    private lateinit var allVisitedAttendances: List<StudentAttendance>
    private lateinit var allValidSkipAttendances: List<StudentAttendance>
    private lateinit var allInvalidSkipAttendances: List<StudentAttendance>

    fun bind(
            student: Student,
            allVisitedAttendances: List<StudentAttendance>,
            allValidSkipAttendances: List<StudentAttendance>,
            allInvalidSkipAttendances: List<StudentAttendance>
    ) {
        this.student = student

        this.allVisitedAttendances = allVisitedAttendances
        this.allValidSkipAttendances = allValidSkipAttendances
        this.allInvalidSkipAttendances = allInvalidSkipAttendances
    }

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        val v = if (convertView == null) {
            MonitoringStudentPaymentItemView_.build(context)
        } else {
            convertView as MonitoringStudentPaymentItemView
        }

        v.bind(
                student = student,
                month = getItem(position),
                allVisitedAttendances = allVisitedAttendances,
                allValidSkipAttendances = allValidSkipAttendances,
                allInvalidSkipAttendances = allInvalidSkipAttendances
        )

        return v
    }

    override fun getItem(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return 12
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_student_payment)
open class MonitoringStudentPaymentActivity : BaseActivity() {
    companion object {
        const val EXTRA_STUDENT_ID = "EXTRA_STUDENT_ID"

        const val LESSONS_START_TIME = 1547413200000
    }

    @Bean
    lateinit var studentsAttendancesService: StudentsAttendancesService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService

    @Bean
    lateinit var monitoringStudentPaymentListAdapter: MonitoringStudentPaymentListAdapter

    @Extra(EXTRA_STUDENT_ID)
    @JvmField
    var studentId: Long = 0

    @AfterViews
    fun init() {
        monitoringStudentPaymentHeaderView
                .setLeftButtonAction { doFinish() }

        val allAttendances = studentsAttendancesService.getAllStudentAttendances(studentId)
        val allVisitedAttendances = allAttendances.filter { it.type != StudentAttendance.Type.VISITED }
        val allValidSkipAttendances = allAttendances.filter { it.type == StudentAttendance.Type.VALID_SKIP }
        val allInvalidSkipAttendances = allAttendances.filter { it.type == StudentAttendance.Type.INVALID_SKIP }

        val student = studentsService.getStudent(studentId) ?: throw RuntimeException()

        monitoringStudentPaymentListAdapter.bind(
                student = student,
                allVisitedAttendances = allVisitedAttendances,
                allValidSkipAttendances = allValidSkipAttendances,
                allInvalidSkipAttendances = allInvalidSkipAttendances
        )

        monitoringStudentPaymentListView.adapter = monitoringStudentPaymentListAdapter
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
