package com.bdev.hengschoolteacher.ui.activities.monitoring

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.GroupType
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance.Type.VALID_SKIP
import com.bdev.hengschoolteacher.data.school.student_payment.StudentPayment
import com.bdev.hengschoolteacher.service.StudentsAttendancesService
import com.bdev.hengschoolteacher.service.StudentsPaymentsService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder.Companion.redirect
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_monitoring_students.*
import kotlinx.android.synthetic.main.view_monitoring_students.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.EViewGroup

class StudentInfo(
    val student: Student,
    val dept: Long
)

@EViewGroup(R.layout.view_monitoring_students)
open class MonitoringStudentsItemView : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(studentInfo: StudentInfo) {
        monitoringStudentsItemNameView.text = studentInfo.student.name

        monitoringStudentsItemMarkView.visibility = if (studentInfo.dept > 0) { View.VISIBLE } else { View.GONE }
    }
}

private class MonitoringStudentsListAdapter(context: Context) : BaseItemsListAdapter<StudentInfo>(context) {
    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        val v = if (convertView == null) {
            MonitoringStudentsItemView_.build(context)
        } else {
            convertView as MonitoringStudentsItemView
        }

        v.bind(getItem(position))

        return v
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_students)
open class MonitoringStudentsActivity : BaseActivity() {
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService
    @Bean
    lateinit var studentsAttendancesService: StudentsAttendancesService

    private var filterEnabled = true

    @AfterViews
    fun init() {
        monitoringPaymentsHeaderView
                .setLeftButtonAction { monitoringPaymentsMenuLayoutView.openMenu() }
                .setSecondRightButtonAction { toggleFilter() }
                .setSecondRightButtonColor(getFilterColor())

        monitoringPaymentsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MONITORING)

        initList()
    }

    private fun initList() {
        val adapter = MonitoringStudentsListAdapter(this)

        adapter.setItems(
                studentsService.getAllStudents()
                        .map {
                            StudentInfo(it, getStudentDept(
                                    it.id,
                                    studentsAttendancesService.getAllAttendances(),
                                    studentsPaymentsService.getAllPayments()
                            ))
                        }
                        .filter { !filterEnabled || it.dept > 0 }
                        .sortedBy { it.student.name }
        )

        monitoringPaymentsListView.adapter = adapter
        monitoringPaymentsListView.setOnItemClickListener { _, _, position, _ ->
            openStudentPayment(adapter.getItem(position).student.id)
        }
    }

    private fun openStudentPayment(studentId: Long) {
        redirect(this)
                .to(MonitoringStudentPaymentActivity_::class.java)
                .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                .withExtra(MonitoringStudentPaymentActivity.EXTRA_STUDENT_ID, studentId)
                .go()
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        monitoringPaymentsHeaderView.setSecondRightButtonColor(getFilterColor())

        initList()
    }

    private fun getFilterColor(): Int {
        return resources.getColor(if (filterEnabled) { R.color.fill_text_basic_action_link } else { R.color.fill_text_basic })
    }

    private fun getStudentDept(
            studentId: Long,
            allAttendances: List<StudentAttendance>,
            allPayments: List<StudentPayment>
    ): Long {
        val studentAttendances = allAttendances.filter { it.studentId == studentId }.filter { it.type != VALID_SKIP }
        val studentPayments = allPayments.filter { it.studentId == studentId }

        val payed = studentAttendances.fold(0L) { amount, payment ->
            if (payment.groupType == GroupType.INDIVIDUAL) {
                amount + 1100L
            } else {
                amount + 700L
            }
        }

        val spent = studentPayments.map { it.amount }.fold(0L) { amount, value -> amount + value }

        return payed - spent
    }
}
