package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.payments

import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragmentData
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButton
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButtons
import com.bdev.hengschoolteacher.ui.views.app.payments.PaymentsViewData

data class ProfilePaymentsFragmentData(
    val visible: Boolean,
    val filterEnabled: Boolean,

    val allPayments: List<ExistingStudentPayment>,
    val filteredPayments: List<ExistingStudentPayment>,
    val paymentsViewData: PaymentsViewData
): BaseContentFragmentData {
    override fun isVisible() = visible

    override fun getHeaderButtons() = AppHeaderButtons(
        button1 = AppHeaderButton(
            iconId = R.drawable.ic_filter,
            toggled = filterEnabled
        ),
        button2 = null,
        button3 = null
    )
}