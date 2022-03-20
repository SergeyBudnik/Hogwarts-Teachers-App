package com.bdev.hengschoolteacher.interactors.alerts.monitoring

import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_debts.StudentsDebtsInteractor
import javax.inject.Inject

interface AlertsMonitoringStudentsInteractor {
    fun haveAlerts(): Boolean
}

class AlertsMonitoringStudentsInteractorImpl @Inject constructor(
    private val studentsStorageInteractor: StudentsStorageInteractor,
    private val studentsDebtsInteractor: StudentsDebtsInteractor
): AlertsMonitoringStudentsInteractor {
    override fun haveAlerts(): Boolean {
        return studentsStorageInteractor
                .getAll()
                .map { studentsDebtsInteractor.getExpectedDebt(it.login) > 0 }
                .fold(false) { amount, value -> amount or value }
    }
}
