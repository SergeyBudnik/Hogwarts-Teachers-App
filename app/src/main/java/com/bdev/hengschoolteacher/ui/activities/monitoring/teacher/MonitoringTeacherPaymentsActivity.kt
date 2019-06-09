package com.bdev.hengschoolteacher.ui.activities.monitoring.teacher

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.async.StudentsPaymentAsyncService
import com.bdev.hengschoolteacher.data.school.student_payment.StudentPayment
import com.bdev.hengschoolteacher.service.StudentsPaymentsService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.HeaderElementsUtils
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.TimeFormatUtils
import kotlinx.android.synthetic.main.activity_monitoring_teacher_payments.*
import kotlinx.android.synthetic.main.view_monitoring_teacher_payments_empty_with_filter.view.*
import kotlinx.android.synthetic.main.view_monitoring_teacher_payments_item.view.*
import org.androidannotations.annotations.*

@EViewGroup(R.layout.view_monitoring_teacher_payments_empty_with_filter)
open class MonitoringTeacherPaymentsEmptyWithFilterView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(disableFilterAction: () -> Unit) {
        monitoringTeacherPaymentsEmptyWithFilterDisableFilterView.setOnClickListener {
            disableFilterAction.invoke()
        }
    }
}

@EViewGroup(R.layout.view_monitoring_teacher_payments_empty)
open class MonitoringTeacherPaymentsEmptyView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}

@EViewGroup(R.layout.view_monitoring_teacher_payments_item)
open class MonitoringTeacherPaymentsItemView : RelativeLayout {
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService

    @Bean
    lateinit var studentsPaymentsAsyncService: StudentsPaymentAsyncService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(studentPayment: StudentPayment): MonitoringTeacherPaymentsItemView {
        monitoringTeacherPaymentsItemAmountView.text = context.getString(
                R.string.amount_in_rub,
                studentPayment.amount
        )

        monitoringTeacherPaymentsItemStudentView.text = studentsService.getStudent(
                studentPayment.studentId
        )?.name ?: ""

        monitoringTeacherPaymentsItemDateView.text = TimeFormatUtils.format(studentPayment.time)

        renderProcessed(
                studentsPaymentsService.getPayment(studentPayment.id)?.processed ?: false
        )

        setOnClickListener { process(studentPayment.id) }

        return this
    }

    private fun process(studentPaymentId: Long) {
        studentsPaymentsAsyncService
                .setPaymentProcessed(studentPaymentId)
                .onSuccess { renderProcessed(it.processed) }
    }

    private fun renderProcessed(processed: Boolean) {
        monitoringTeacherPaymentsItemProcessedView.setImageDrawable(
                context.resources.getDrawable(if (processed) {
                    R.drawable.ic_tick
                } else {
                    R.drawable.ic_question
                })
        )

        monitoringTeacherPaymentsItemProcessedView.setColorFilter(
                context.resources.getColor(if (processed) {
                    R.color.fill_text_basic_positive
                } else {
                    R.color.fill_text_basic_negative
                })
        )
    }
}

class MonitoringTeacherPaymentsListAdapter(context: Context) : BaseItemsListAdapter<StudentPayment>(context) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView == null) {
            MonitoringTeacherPaymentsItemView_.build(context)
        } else {
            convertView as MonitoringTeacherPaymentsItemView
        }.bind(getItem(position))
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_teacher_payments)
open class MonitoringTeacherPaymentsActivity : BaseActivity() {
    companion object {
        const val EXTRA_TEACHER_ID = "EXTRA_TEACHER_ID"

        fun redirect(current: BaseActivity, teacherId: Long): RedirectBuilder {
            return RedirectBuilder
                    .redirect(current)
                    .to(MonitoringTeacherPaymentsActivity_::class.java)
                    .withExtra(EXTRA_TEACHER_ID, teacherId)
        }
    }

    @Bean
    lateinit var studentPaymentsService: StudentsPaymentsService

    @Extra(EXTRA_TEACHER_ID)
    @JvmField
    var teacherId: Long = 0

    private var filterEnabled: Boolean = true

    @AfterViews
    fun init() {
        monitoringTeacherPaymentsHeaderView
                .setLeftButtonAction { doFinish() }
                .setFirstRightButtonAction { toggleFilter() }
                .setFirstRightButtonColor(getFilterColor())

        monitoringTeacherPaymentsSecondaryHeaderView.bind(
                teacherId = teacherId
        )

        monitoringTeacherPaymentsTeacherInfoView.bind(
                teacherId = teacherId
        )

        monitoringTeacherPaymentsEmptyWithFilterView.bind {
            toggleFilter()
        }

        initList()
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        monitoringTeacherPaymentsHeaderView.setFirstRightButtonColor(getFilterColor())

        initList()
    }

    private fun getFilterColor() : Int {
        return HeaderElementsUtils.getColor(this, filterEnabled)
    }

    private fun initList() {
        val allPayments = studentPaymentsService.getPaymentsToTeacher(teacherId)
        val filteredPayments = allPayments.filter { payment -> !filterEnabled || !payment.processed }

        monitoringTeacherPaymentsEmptyView.visibility =
                if (allPayments.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

        monitoringTeacherPaymentsEmptyWithFilterView.visibility =
                if (!allPayments.isEmpty() && filteredPayments.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

        MonitoringTeacherPaymentsListAdapter(this).let {
            it.setItems(filteredPayments)

            monitoringTeacherPaymentsListView.adapter = it
        }
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
