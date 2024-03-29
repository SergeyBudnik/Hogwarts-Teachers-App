package com.bdev.hengschoolteacher.ui.page_fragments.student.statistics

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
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
import com.bdev.hengschoolteacher.ui.fragments.student.header.StudentHeaderFragmentData
import com.bdev.hengschoolteacher.ui.fragments.student.header.data.StudentHeaderFragmentItem
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
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
//            MonitoringStudentMonthAttendancePageFragment.redirectToChild(
//                current = context as BasePageFragment,
//                studentLogin = data.student.login,
//                monthIndex = data.month
//            )
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
class StudentStatisticsPageFragment : BasePageFragment<StudentStatisticsPageFragmentViewModel>() {
    private val args: StudentStatisticsPageFragmentArgs by navArgs()

    @Inject lateinit var studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractor
    @Inject lateinit var lessonsService: LessonsInteractor
    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var studentsDebtsInteractor: StudentsDebtsInteractor

    @Inject lateinit var studentPricingInteractor: StudentsPricingInteractor
    @Inject lateinit var studentsPaymentsProviderInteractor: StudentsPaymentsProviderInteractor

    override fun provideViewModel(): StudentStatisticsPageFragmentViewModel =
        ViewModelProvider(this).get(StudentStatisticsPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_monitoring_lessons, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        // studentLogin = intent.getStringExtra(EXTRA_STUDENT_LOGIN)!! todo

        val student = studentsStorageInteractor.getByLogin(args.args.login)

        monitoringStudentPaymentHeaderView
                .setTitle("Студент. ${student?.person?.name ?: "?"}")
                .setLeftButtonAction { doFinish() }

        monitoringStudentPaymentSecondaryHeaderView.bind(
            data = StudentHeaderFragmentData(
                login = args.args.login,
                item = StudentHeaderFragmentItem.ATTENDANCE,
            ),
            navCommandHandler = {}
        )

        monitoringStudentPaymentDeptView.text = "${studentsDebtsInteractor.getDebt(args.args.login)}"

        monitoringStudentExpectedMonthlyDebtView.text = "${studentsDebtsInteractor.getExpectedDebt(studentLogin = args.args.login)}"

        val allAttendances = studentsAttendancesProviderInteractor.getAllStudentAttendances(args.args.login)
        val allVisitedAttendances = allAttendances.filter { it.type == StudentAttendanceType.VISITED }
        val allValidSkipAttendances = allAttendances.filter { it.type == StudentAttendanceType.VALID_SKIP }
        val allInvalidSkipAttendances = allAttendances.filter { it.type == StudentAttendanceType.INVALID_SKIP }
        val allFreeLessonAttendances = allAttendances.filter { it.type == StudentAttendanceType.FREE_LESSON }

        val monitoringStudentPaymentListAdapter = MonitoringStudentPaymentListAdapter(context = requireContext())

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

    private fun doFinish() {
//        finish()
//        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
