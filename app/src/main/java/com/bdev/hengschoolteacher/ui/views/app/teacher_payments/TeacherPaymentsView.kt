package com.bdev.hengschoolteacher.ui.views.app.teacher_payments

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.async.StudentsPaymentAsyncService
import com.bdev.hengschoolteacher.data.school.student_payment.StudentPayment
import com.bdev.hengschoolteacher.service.StudentsPaymentsService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.TimeFormatUtils
import kotlinx.android.synthetic.main.view_teacher_payments.view.*
import kotlinx.android.synthetic.main.view_teacher_payments_item.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_teacher_payments_item)
open class TeacherPaymentsItemView : RelativeLayout {
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService

    @Bean
    lateinit var studentsPaymentsAsyncService: StudentsPaymentAsyncService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(studentPayment: StudentPayment, editable: Boolean): TeacherPaymentsItemView {
        teacherPaymentsItemAmountView.text = context.getString(
                R.string.amount_in_rub,
                studentPayment.amount
        )

        teacherPaymentsItemStudentView.text = studentsService.getStudent(
                studentPayment.studentId
        )?.name ?: ""

        teacherPaymentsItemDateView.text = TimeFormatUtils.format(studentPayment.time)

        renderProcessed(
                studentsPaymentsService.getPayment(studentPayment.id)?.processed ?: false
        )

        setOnClickListener {
            if (editable) {
                process(studentPayment.id)
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
        teacherPaymentsItemProcessedView.setImageDrawable(
                context.resources.getDrawable(if (processed) {
                    R.drawable.ic_tick
                } else {
                    R.drawable.ic_question
                })
        )

        teacherPaymentsItemProcessedView.setColorFilter(
                context.resources.getColor(if (processed) {
                    R.color.fill_text_basic_positive
                } else {
                    R.color.fill_text_basic_negative
                })
        )
    }
}

class TeacherPaymentsListAdapter(
        private val editable: Boolean,
        context: Context
) : BaseItemsListAdapter<StudentPayment>(context) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView == null) {
            TeacherPaymentsItemView_.build(context)
        } else {
            convertView as TeacherPaymentsItemView
        }.bind(getItem(position), editable)
    }
}

@EViewGroup(R.layout.view_teacher_payments)
open class TeacherPaymentsView : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(
            payments: List<StudentPayment>,
            editable: Boolean
    ) {
        TeacherPaymentsListAdapter(editable, context).let {
            it.setItems(payments.sortedByDescending { payment -> payment.time })

            teacherPaymentsListView.adapter = it
        }
    }
}