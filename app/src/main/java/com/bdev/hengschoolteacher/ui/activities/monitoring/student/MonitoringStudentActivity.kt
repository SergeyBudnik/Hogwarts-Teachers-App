package com.bdev.hengschoolteacher.ui.activities.monitoring.student

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
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.services.students_debts.StudentDebtsService
import com.bdev.hengschoolteacher.services.lessons.LessonsService
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.services.students_attendances.StudentsAttendancesProviderService
import com.bdev.hengschoolteacher.services.students_debts.StudentDebtsServiceImpl
import com.bdev.hengschoolteacher.services.students_payments.StudentsPaymentsProviderService
import com.bdev.hengschoolteacher.services.students_payments.StudentsPaymentsProviderServiceImpl
import com.bdev.hengschoolteacher.services.students_pricing.StudentsPricingService
import com.bdev.hengschoolteacher.services.students_pricing.StudentsPricingServiceImpl
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.student.StudentHeaderItem
import com.bdev.hengschoolteacher.utils.TimeUtils
import kotlinx.android.synthetic.main.activity_monitoring_student_payment.*
import kotlinx.android.synthetic.main.view_monitoring_student_payment_item.view.*
import org.androidannotations.annotations.*

@EViewGroup(R.layout.view_monitoring_student_payment_item)
open class MonitoringStudentPaymentItemView : RelativeLayout {
    @Bean(StudentsPaymentsProviderServiceImpl::class)
    lateinit var studentsPaymentsProviderService: StudentsPaymentsProviderService
    @Bean(StudentsPricingServiceImpl::class)
    lateinit var studentsPricingService: StudentsPricingService
    @Bean
    lateinit var lessonsService: LessonsService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(
            student: Student,
            month: Int,
            allVisitedAttendances: List<StudentAttendance>,
            allValidSkipAttendances: List<StudentAttendance>,
            allInvalidSkipAttendances: List<StudentAttendance>,
            allFreeLessonAttendances: List<StudentAttendance>
    ) {
        val totalLessonsAmount = lessonsService.getAllStudentLessonsInMonth(
                studentLogin = student.login,
                month = month
        ).size

        val visitedLessonsAmount = getLessonsAmount(allVisitedAttendances, month)
        val validSkipLessonsAmount = getLessonsAmount(allValidSkipAttendances, month)
        val invalidSkipLessonsAmount = getLessonsAmount(allInvalidSkipAttendances, month)
        val freeLessonsAmount = getLessonsAmount(allFreeLessonAttendances, month)

        monitoringStudentPaymentItemMonthView.text = resources.getString(Month.findByIndex(month).nameId)

        monitoringStudentPaymentItemTotalLessonsAmountView.text = "$totalLessonsAmount"
        monitoringStudentPaymentItemVisitedLessonsAmountView.text = "$visitedLessonsAmount"
        monitoringStudentPaymentItemValidSkipLessonsAmountView.text = "$validSkipLessonsAmount"
        monitoringStudentPaymentItemInvalidSkipLessonsAmountView.text = "$invalidSkipLessonsAmount"
        monitoringStudentPaymentItemFreeLessonLessonsAmountView.text = "$freeLessonsAmount"

        monitoringStudentPaymentItemPayedAmountView.text = "${studentsPaymentsProviderService.getForStudentForMonth(
                studentLogin = student.login,
                monthIndex = month
        ).map { it.info.amount }.fold(0L) { p1, p2 -> p1 + p2 }}"

        monitoringStudentPaymentItemSpentAmountView.text = "${studentsPricingService.getMonthPrice(
                studentLogin = student.login,
                month = month
        )}"

        setOnClickListener {
            MonitoringStudentMonthAttendanceActivity.redirectToChild(
                    current = context as BaseActivity,
                    studentLogin = student.login,
                    monthIndex = month
            )
        }
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
}

@EBean
open class MonitoringStudentPaymentListAdapter : BaseAdapter() {
    @RootContext
    lateinit var context: Context

    private lateinit var student: Student
    private lateinit var allVisitedAttendances: List<StudentAttendance>
    private lateinit var allValidSkipAttendances: List<StudentAttendance>
    private lateinit var allInvalidSkipAttendances: List<StudentAttendance>
    private lateinit var allFreeLessonAttendances: List<StudentAttendance>

    fun bind(
            student: Student,
            allVisitedAttendances: List<StudentAttendance>,
            allValidSkipAttendances: List<StudentAttendance>,
            allInvalidSkipAttendances: List<StudentAttendance>,
            allFreeLessonAttendances: List<StudentAttendance>
    ) {
        this.student = student

        this.allVisitedAttendances = allVisitedAttendances
        this.allValidSkipAttendances = allValidSkipAttendances
        this.allInvalidSkipAttendances = allInvalidSkipAttendances
        this.allFreeLessonAttendances = allFreeLessonAttendances
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
                allInvalidSkipAttendances = allInvalidSkipAttendances,
                allFreeLessonAttendances = allFreeLessonAttendances
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
open class MonitoringStudentActivity : BaseActivity() {
    companion object {
        const val EXTRA_STUDENT_LOGIN = "EXTRA_STUDENT_LOGIN"

        fun redirectToChild(current: BaseActivity, studentLogin: String) {
            redirect(current, studentLogin)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }

        fun redirectToSibling(current: BaseActivity, studentLogin: String) {
            redirect(current, studentLogin)
                    .withAnim(0, 0)
                    .goAndCloseCurrent()
        }

        private fun redirect(current: BaseActivity, studentLogin: String): RedirectBuilder {
            return RedirectBuilder
                    .redirect(current)
                    .to(MonitoringStudentActivity_::class.java)
                    .withExtra(EXTRA_STUDENT_LOGIN, studentLogin)
        }
    }

    @Bean
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService
    @Bean(StudentDebtsServiceImpl::class)
    lateinit var studentsDebtsService: StudentDebtsService

    @Bean
    lateinit var monitoringStudentPaymentListAdapter: MonitoringStudentPaymentListAdapter

    @Bean(StudentsPricingServiceImpl::class)
    lateinit var studentPricingService: StudentsPricingService

    @Extra(EXTRA_STUDENT_LOGIN)
    lateinit var studentLogin: String

    @AfterViews
    fun init() {
        val student = studentsStorageService.getByLogin(studentLogin)

        monitoringStudentPaymentHeaderView
                .setTitle("Студент. ${student?.person?.name ?: "?"}")
                .setLeftButtonAction { doFinish() }

        monitoringStudentPaymentSecondaryHeaderView.bind(
                item = StudentHeaderItem.ATTENDANCE,
                studentLogin = studentLogin
        )

        monitoringStudentPaymentDeptView.text = "${studentsDebtsService.getDebt(studentLogin)}"

        monitoringStudentExpectedMonthlyDebtView.text = "${studentsDebtsService.getExpectedDebt(studentLogin = studentLogin)}"

        val allAttendances = studentsAttendancesProviderService.getAllStudentAttendances(studentLogin)
        val allVisitedAttendances = allAttendances.filter { it.type == StudentAttendanceType.VISITED }
        val allValidSkipAttendances = allAttendances.filter { it.type == StudentAttendanceType.VALID_SKIP }
        val allInvalidSkipAttendances = allAttendances.filter { it.type == StudentAttendanceType.INVALID_SKIP }
        val allFreeLessonAttendances = allAttendances.filter { it.type == StudentAttendanceType.FREE_LESSON }

        student?.let {
            monitoringStudentPaymentListAdapter.bind(
                    student = it,
                    allVisitedAttendances = allVisitedAttendances,
                    allValidSkipAttendances = allValidSkipAttendances,
                    allInvalidSkipAttendances = allInvalidSkipAttendances,
                    allFreeLessonAttendances = allFreeLessonAttendances
            )

            monitoringStudentPaymentListView.adapter = monitoringStudentPaymentListAdapter
        }
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
