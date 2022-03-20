package com.bdev.hengschoolteacher.interactors.students_payments

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask
import com.bdev.hengschoolteacher.network.api.students_payments.StudentsPaymentsApiProvider
import javax.inject.Inject

interface StudentsPaymentsLoadingInteractor {
    fun load(): SmartPromise<Unit, Exception>
}

class StudentsPaymentsLoadingInteractorImpl @Inject constructor(
    private val studentsPaymentsApiProvider: StudentsPaymentsApiProvider,
    private val studentsPaymentsModifierInteractor: StudentsPaymentsModifierInteractor
): StudentsPaymentsLoadingInteractor {
    override fun load(): SmartPromise<Unit, Exception> {
        return SmartTask.smartTask {
            studentsPaymentsModifierInteractor.setAll(
                payments = studentsPaymentsApiProvider.provide().getStudentsPayments().execute().body()!!
            )
        }
    }
}