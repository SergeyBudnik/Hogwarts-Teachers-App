package com.bdev.hengschoolteacher.ui.views.app.payments

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.resources.AppResources
import com.bdev.hengschoolteacher.ui.utils.TimeFormatUtils
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import kotlinx.android.synthetic.main.view_payments.view.*
import kotlinx.android.synthetic.main.view_payments_item.view.*
import kotlinx.android.synthetic.main.view_payments_summary.view.*

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

data class PaymentsItemViewData(
        val studentPayment: ExistingStudentPayment,
        val studentName: String,
        val staffMemberName: String,
        val singleTeacher: Boolean,
        val clickAction: (() -> Unit)?
)

class PaymentsItemView : RelativeLayout {
    init {
        R.layout.view_payments_item
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(data: PaymentsItemViewData): PaymentsItemView {
        paymentsItemAmountView.text = context.getString(
                R.string.amount_in_rub,
                data.studentPayment.info.amount
        )

        paymentsItemStudentView.text = data.studentName

        paymentsItemTeacherView.text = data.staffMemberName

        paymentsItemDateView.text = TimeFormatUtils.format(data.studentPayment.info.time)

        paymentsItemTeacherView.visibility = visibleElseGone(visible = !data.singleTeacher)

        renderProcessed(data.studentPayment.processed)

        setOnClickListener {
            data.clickAction?.invoke()
        }

        return this
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

class PaymentsListAdapter(context: Context) : BaseItemsListAdapter<PaymentsItemViewData>(context) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView == null) {
            PaymentsItemView(context)
        } else {
            convertView as PaymentsItemView
        }.bind(data = getItem(position))
    }
}

data class PaymentsViewData(
        val paymentItemsData: List<PaymentsItemViewData>
)

class PaymentsView : RelativeLayout {
    init {
        View.inflate(context, R.layout.view_payments, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(data: PaymentsViewData) {
        // paymentsSummaryView.bind(payments = payments) todo

        PaymentsListAdapter(context = context).let {
            // payments

            it.setItems(data.paymentItemsData)

            paymentsListView.adapter = it
        }
    }
}