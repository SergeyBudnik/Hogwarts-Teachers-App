package com.bdev.hengschoolteacher.ui.activities.monitoring.student

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
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
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderInteractor
import com.bdev.hengschoolteacher.interactors.students_debts.StudentsDebtsInteractor
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderInteractor
import com.bdev.hengschoolteacher.interactors.students_pricing.StudentsPricingInteractor
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.student.StudentHeaderItem
import com.bdev.hengschoolteacher.utils.TimeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_monitoring_student_payment.*
import kotlinx.android.synthetic.main.view_monitoring_student_payment_item.view.*
import javax.inject.Inject

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

class MonitoringStudentPaymentListAdapter(
    private val context: Context
): BaseAdapter() {
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

@AndroidEntryPoint
class MonitoringStudentActivity : BaseActivity() {
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
                    .to(MonitoringStudentActivity::class.java)
                    .withExtra(EXTRA_STUDENT_LOGIN, studentLogin)
        }
    }

    @Inject lateinit var studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractor
    @Inject lateinit var lessonsService: LessonsInteractor
    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var studentsDebtsInteractor: StudentsDebtsInteractor

    @Inject lateinit var studentPricingInteractor: StudentsPricingInteractor
    @Inject lateinit var studentsPaymentsProviderInteractor: StudentsPaymentsProviderInteractor

    lateinit var studentLogin: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_monitoring_student_payment)

        studentLogin = intent.getStringExtra(EXTRA_STUDENT_LOGIN)!!

        val student = studentsStorageInteractor.getByLogin(studentLogin)

        monitoringStudentPaymentHeaderView
                .setTitle("Студент. ${student?.person?.name ?: "?"}")
                .setLeftButtonAction { doFinish() }

        monitoringStudentPaymentSecondaryHeaderView.bind(
                item = StudentHeaderItem.ATTENDANCE,
                studentLogin = studentLogin
        )

        monitoringStudentPaymentDeptView.text = "${studentsDebtsInteractor.getDebt(studentLogin)}"

        monitoringStudentExpectedMonthlyDebtView.text = "${studentsDebtsInteractor.getExpectedDebt(studentLogin = studentLogin)}"

        val allAttendances = studentsAttendancesProviderInteractor.getAllStudentAttendances(studentLogin)
        val allVisitedAttendances = allAttendances.filter { it.type == StudentAttendanceType.VISITED }
        val allValidSkipAttendances = allAttendances.filter { it.type == StudentAttendanceType.VALID_SKIP }
        val allInvalidSkipAttendances = allAttendances.filter { it.type == StudentAttendanceType.INVALID_SKIP }
        val allFreeLessonAttendances = allAttendances.filter { it.type == StudentAttendanceType.FREE_LESSON }

        val monitoringStudentPaymentListAdapter = MonitoringStudentPaymentListAdapter(context = this)

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
                                moneySpent = studentPricingInteractor.getMonthPrice(
                                        studentLogin = student.login,
                                        month = month
                                ).toInt(),
                                moneyPayed = studentsPaymentsProviderInteractor.getForStudentForMonth(
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
