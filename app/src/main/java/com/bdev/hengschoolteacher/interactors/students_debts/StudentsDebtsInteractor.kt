package com.bdev.hengschoolteacher.interactors.students_debts

import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderInteractor
import com.bdev.hengschoolteacher.interactors.students_pricing.StudentsPricingInteractor
import javax.inject.Inject

interface StudentsDebtsInteractor {
    fun getExpectedDebt(studentLogin: String): Int
    fun getDebt(studentLogin: String): Int
}

class StudentsDebtsInteractorImpl @Inject constructor(
    private val studentsPaymentsProviderInteractor: StudentsPaymentsProviderInteractor,
    private val studentsPricingInteractor: StudentsPricingInteractor
): StudentsDebtsInteractor {
    override fun getExpectedDebt(studentLogin: String): Int {
        val payed = getPaymentsAmount(studentLogin = studentLogin)
        val price = studentsPricingInteractor.getTotalExpectedPrice(studentLogin = studentLogin)

        return (price - payed).toInt()
    }

    override fun getDebt(studentLogin: String): Int {
        val payed = getPaymentsAmount(studentLogin = studentLogin)
        val price = studentsPricingInteractor.getTotalPrice(studentLogin = studentLogin)

        return (price - payed).toInt()
    }

    private fun getPaymentsAmount(studentLogin: String): Int {
        return studentsPaymentsProviderInteractor
                .getForStudent(studentLogin = studentLogin)
                .fold(0L) { amount, value -> amount + value.info.amount }
                .toInt()
    }
}
