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
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractorImpl
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderServiceImpl
import com.bdev.hengschoolteacher.interactors.students_debts.StudentDebtsService
import com.bdev.hengschoolteacher.interactors.students_debts.StudentDebtsServiceImpl
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderService
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderServiceImpl
import com.bdev.hengschoolteacher.interactors.students_pricing.StudentsPricingService
import com.bdev.hengschoolteacher.interactors.students_pricing.StudentsPricingServiceImpl
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.student.StudentHeaderItem
import com.bdev.hengschoolteacher.utils.TimeUtils
import kotlinx.android.synthetic.main.activity_monitoring_student_payment.*
import kotlinx.android.synthetic.main.view_monitoring_student_payment_item.view.*
import org.androidannotations.annotations.*

data class MonitoringStudentPaymentItemViewData(
        val student: Student,
        val month: Int,
        val totalLessonsAmount: Int,
        val moneySpent: Int,
        val moneyPayed: Int,
        val allVisitedAttendances: List<StudentAttendance>,
        val allValidSkipAttendances: List<StudentAttendance>,
        val allInvalidSkipAttendances: List<StudentAttendance>,
        val allFreeLessonAttendances: List<StudentAttendance>
)

class MonitoringStudentPaymentItemView : RelativeLayout {
    init {
        View.inflate(context, R.layout.view_monitoring_student_payment_item, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(data: MonitoringStudentPaymentItemViewData) {
        val visitedLessonsAmount = getLessonsAmount(data.allVisitedAttendances, data.month)
        val validSkipLessonsAmount = getLessonsAmount(data.allValidSkipAttendances, data.month)
        val invalidSkipLessonsAmount = getLessonsAmount(data.allInvalidSkipAttendances, data.month)
        val freeLessonsAmount = getLessonsAmount(data.allFreeLessonAttendances, data.month)

        monitoringStudentPaymentItemMonthView.text = resources.getString(Month.findByIndex(data.month).nameId)

        monitoringStudentPaymentItemTotalLessonsAmountView.text = "${data.totalLessonsAmount}"
        monitoringStudentPaymentItemVisitedLessonsAmountView.text = "$visitedLessonsAmount"
        monitoringStudentPaymentItemValidSkipLessonsAmountView.text = "$validSkipLessonsAmount"
        monitoringStudentPaymentItemInvalidSkipLessonsAmountView.text = "$invalidSkipLessonsAmount"
        monitoringStudentPaymentItemFreeLessonLessonsAmountView.text = "$freeLessonsAmount"

        monitoringStudentPaymentItemPayedAmountView.text = "${data.moneyPayed}"
        monitoringStudentPaymentItemSpentAmountView.text = "${data.moneySpent}"

        setOnClickListener {
            MonitoringStudentMonthAttendanceActivity.redirectToChild(
                    current = context as BaseActivity,
                    studentLogin = data.student.login,
                    monthIndex = data.month
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

    private var items: List<MonitoringStudentPaymentItemViewData> = emptyList()

    fun bind(items: List<MonitoringStudentPaymentItemViewData>) {
        this.items = items
    }

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        val v = if (convertView == null) {
            MonitoringStudentPaymentItemView(context)
        } else {
            convertView as MonitoringStudentPaymentItemView
        }

        v.bind(data = getItem(position))

        return v
    }

    override fun getItem(position: Int): MonitoringStudentPaymentItemViewData {
        return items[position]
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
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderServiceImpl
    @Bean
    lateinit var lessonsService: LessonsInteractorImpl
    @Bean(StudentsStorageInteractorImpl::class)
    lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Bean(StudentDebtsServiceImpl::class)
    lateinit var studentsDebtsService: StudentDebtsService

    @Bean
    lateinit var monitoringStudentPaymentListAdapter: MonitoringStudentPaymentListAdapter

    @Bean(StudentsPricingServiceImpl::class)
    lateinit var studentPricingService: StudentsPricingService
    @Bean(StudentsPaymentsProviderServiceImpl::class)
    lateinit var studentsPaymentsProviderService: StudentsPaymentsProviderService

    @Extra(EXTRA_STUDENT_LOGIN)
    lateinit var studentLogin: String

    @AfterViews
    fun init() {
        val student = studentsStorageInteractor.getByLogin(studentLogin)

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
                    items = (0 until 12).map { month ->
                        MonitoringStudentPaymentItemViewData(
                                student = it,
                                month = month,
                                totalLessonsAmount = lessonsService.getAllStudentLessonsInMonth(
                                        studentLogin = student.login,
                                        month = month
                                ).size,
                                moneySpent = studentPricingService.getMonthPrice(
                                        studentLogin = student.login,
                                        month = month
                                ).toInt(),
                                moneyPayed = studentsPaymentsProviderService.getForStudentForMonth(
                                        studentLogin = student.login,
                                        monthIndex = month
                                ).map { it.info.amount }.fold(0L) { p1, p2 -> p1 + p2 }.toInt(),
                                allVisitedAttendances = allVisitedAttendances,
                                allValidSkipAttendances = allValidSkipAttendances,
                                allInvalidSkipAttendances = allInvalidSkipAttendances,
                                allFreeLessonAttendances = allFreeLessonAttendances
                        )
                    }
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
