package com.bdev.hengschoolteacher.interactors.students_attendances

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.network.api.students_attendances.StudentsAttendancesApiProvider
import javax.inject.Inject

interface StudentsAttendancesModifierInteractor {
    fun addAttendance(attendance: StudentAttendance): SmartPromise<Unit, Exception>
}

open class StudentsAttendancesModifierInteractorImpl @Inject constructor(
    private val studentsAttendancesApiProvider: StudentsAttendancesApiProvider,
    private val studentsAttendancesStorageInteractor: StudentsAttendancesStorageInteractor
): StudentsAttendancesModifierInteractor {
    override fun addAttendance(attendance: StudentAttendance): SmartPromise<Unit, Exception> {
        return smartTask {
            studentsAttendancesApiProvider
                .provide()
                .addStudentAttendance(attendance = attendance)
                .execute()

            studentsAttendancesStorageInteractor.add(studentAttendance = attendance)
        }
    }
}