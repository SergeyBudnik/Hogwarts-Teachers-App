package com.bdev.hengschoolteacher.ui.views.app.payments

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.async.StudentsPaymentAsyncService
import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import com.bdev.hengschoolteacher.services.staff.StaffMembersStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.services.students_payments.StudentsPaymentsProviderService
import com.bdev.hengschoolteacher.services.students_payments.StudentsPaymentsProviderServiceImpl
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.resources.AppResources
import com.bdev.hengschoolteacher.ui.utils.TimeFormatUtils
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import kotlinx.android.synthetic.main.view_payments.view.*
import kotlinx.android.synthetic.main.view_payments_item.view.*
import kotlinx.android.synthetic.main.view_payments_summary.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

class PaymentsSummaryView : RelativeLayout {
    init {
        View.inflate(context, R.layout.view_payments_summary, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(payments: List<ExistingStudentPayment>) {
        bindAmount(payments = payments)
        bindPeriod(payments = payments)
    }

    private fun bindAmount(payments: List<ExistingStudentPayment>) {
        val amount = payments
                .filter { !it.processed }
                .map { it.info.amount }
                .fold(0L) { res, value -> res + value }

        paymentsSummaryAmountView.text = context.getString(R.string.amount_in_rub, amount)
    }

    private fun bindPeriod(payments: List<ExistingStudentPayment>) {
        val earliestPayment = payments.minBy { it.info.time }
        val latestPayment = payments.maxBy { it.info.time }

        if (earliestPayment != null && latestPayment != null) {
            paymentsSummaryPeriodView.visibility = View.VISIBLE

            paymentsSummaryPeriodView.text = context.getString(
                    R.string.payments_summary_period,
                    TimeFormatUtils.formatOnlyDate(earliestPayment.info.time),
                    TimeFormatUtils.formatOnlyDate(latestPayment.info.time)
            )
        } else {
            paymentsSummaryPeriodView.visibility = View.GONE
        }
    }
}

@EViewGroup(R.layout.view_payments_item)
open class PaymentsItemView : RelativeLayout {
    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService
    @Bean(StudentsPaymentsProviderServiceImpl::class)
    lateinit var studentsPaymentsProviderService: StudentsPaymentsProviderService
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageService

    @Bean
    lateinit var studentsPaymentsAsyncService: StudentsPaymentAsyncService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(existingStudentPayment: ExistingStudentPayment, singleTeacher: Boolean, editable: Boolean): PaymentsItemView {
        paymentsItemAmountView.text = context.getString(
                R.string.amount_in_rub,
                existingStudentPayment.info.amount
        )

        paymentsItemStudentView.text = studentsStorageService.getByLogin(
                existingStudentPayment.info.studentLogin
        )?.person?.name ?: "?"

        paymentsItemTeacherView.text = staffMembersStorageService.getStaffMember(
                existingStudentPayment.info.staffMemberLogin
        )?.person?.name ?: "?"

        paymentsItemDateView.text = TimeFormatUtils.format(existingStudentPayment.info.time)

        paymentsItemTeacherView.visibility = visibleElseGone(visible = !singleTeacher)

        renderProcessed(
                studentsPaymentsProviderService.getByPaymentId(existingStudentPayment.id)?.processed ?: false
        )

        setOnClickListener {
            if (editable) {
                process(existingStudentPayment.id)
            }
        }

        return this
    }

    private fun process(studentPaymentId: Long) {
        studentsPaymentsAsyncService
                .setPaymentProcessed(studentPaymentId)
                .onSuccess { renderProcessed(it.processed) }
    }

    private fun renderProcessed(processed: Boolean) {
        paymentsItemProcessedView.setImageDrawable(
                AppResources.getDrawable(
                        context = context,
                        drawableId = if (processed) {
                            R.drawable.ic_tick
                        } else {
                            R.drawable.ic_question
                        }
                )
        )

        paymentsItemProcessedView.setColorFilter(
                AppResources.getColor(
                        context = context,
                        colorId = if (processed) {
                            R.color.fill_text_basic_positive
                        } else {
                            R.color.fill_text_basic_negative
                        }
                )
        )
    }
}

class PaymentsListAdapter(
        private val singleTeacher: Boolean,
        private val editable: Boolean,
        context: Context
) : BaseItemsListAdapter<ExistingStudentPayment>(context) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView == null) {
            PaymentsItemView_.build(context)
        } else {
            convertView as PaymentsItemView
        }.bind(
                existingStudentPayment = getItem(position),
                singleTeacher = singleTeacher,
                editable = editable
        )
    }
}

class PaymentsView : RelativeLayout {
    init {
        View.inflate(context, R.layout.view_payments, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(
            payments: List<ExistingStudentPayment>,
            singleTeacher: Boolean,
            editable: Boolean
    ) {
        paymentsSummaryView.bind(payments = payments)

        PaymentsListAdapter(
                singleTeacher = singleTeacher,
                editable = editable,
                context = context
        ).let {
            it.setItems(payments.sortedByDescending { payment -> payment.info.time })

            paymentsListView.adapter = it
        }
    }
}