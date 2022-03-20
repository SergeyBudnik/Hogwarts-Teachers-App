package com.bdev.hengschoolteacher.interactors.students_attendances

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.network.api.students_attendances.StudentsAttendancesApiProvider
import javax.inject.Inject

interface StudentsAttendancesLoadingInteractor {
    fun load(): SmartPromise<Unit, Exception>
}

class StudentsAttendancesLoadingInteractorImpl @Inject constructor(
    private val studentsAttendancesApiProvider: StudentsAttendancesApiProvider,
    private val studentsAttendancesStorageInteractor: StudentsAttendancesStorageInteractor
): StudentsAttendancesLoadingInteractor {
    override fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            studentsAttendancesStorageInteractor.setAll(
                    studentsAttendancesApiProvider.provide().getStudentsAttendances().execute().body()!!
            )
        }
    }
}