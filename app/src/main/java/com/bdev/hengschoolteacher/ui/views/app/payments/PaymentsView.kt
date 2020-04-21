package com.bdev.hengschoolteacher.ui.views.app.payments

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.async.StudentsPaymentAsyncService
import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import com.bdev.hengschoolteacher.service.StudentsPaymentsService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.service.staff.StaffMembersStorageService
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.TimeFormatUtils
import kotlinx.android.synthetic.main.view_payments.view.*
import kotlinx.android.synthetic.main.view_payments_item.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_payments_item)
open class PaymentsItemView : RelativeLayout {
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService
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

        paymentsItemStudentView.text = studentsService.getStudent(
                existingStudentPayment.info.studentLogin
        )?.person?.name ?: "?"

        paymentsItemTeacherView.text = staffMembersStorageService.getStaffMember(
                existingStudentPayment.info.staffMemberLogin
        )?.person?.name ?: "?"

        paymentsItemDateView.text = TimeFormatUtils.format(existingStudentPayment.info.time)

        paymentsItemTeacherView.visibility = if (singleTeacher) { View.GONE } else { View.VISIBLE }

        renderProcessed(
                studentsPaymentsService.getPayment(existingStudentPayment.id)?.processed ?: false
        )

        setOnClickListener {
            if (editable) {
                process(existingStudentPayment.id)
            } else { /* Do nothing */ }
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
                context.resources.getDrawable(if (processed) {
                    R.drawable.ic_tick
                } else {
                    R.drawable.ic_question
                })
        )

        paymentsItemProcessedView.setColorFilter(
                context.resources.getColor(if (processed) {
                    R.color.fill_text_basic_positive
                } else {
                    R.color.fill_text_basic_negative
                })
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

@EViewGroup(R.layout.view_payments)
open class PaymentsView : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(
            paymentExistings: List<ExistingStudentPayment>,
            singleTeacher: Boolean,
            editable: Boolean
    ) {
        PaymentsListAdapter(
                singleTeacher = singleTeacher,
                editable = editable,
                context = context
        ).let {
            it.setItems(paymentExistings.sortedByDescending { payment -> payment.info.time })

            paymentsListView.adapter = it
        }
    }
}