package com.bdev.hengschoolteacher.interactors.students_attendances

import com.bdev.hengschoolteacher.dao.StudentsAttendancesDaoImpl
import com.bdev.hengschoolteacher.dao.StudentsAttendancesModel
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import javax.inject.Inject

interface StudentsAttendancesStorageInteractor {
    fun getAll(): List<StudentAttendance>
    fun setAll(studentsAttendances: List<StudentAttendance>)
    fun add(studentAttendance: StudentAttendance)
}

class StudentsAttendancesStorageInteractorImpl @Inject constructor(
    private val studentsAttendancesDao: StudentsAttendancesDaoImpl
): StudentsAttendancesStorageInteractor {
    override fun getAll(): List<StudentAttendance> {
        return studentsAttendancesDao
                .readValue()
                .studentsAttendances
    }

    override fun setAll(studentsAttendances: List<StudentAttendance>) {
        studentsAttendancesDao.writeValue(
                StudentsAttendancesModel(studentsAttendances = studentsAttendances)
        )
    }

    override fun add(studentAttendance: StudentAttendance) {
        val res = studentsAttendancesDao
                .readValue()
                .studentsAttendances
                .filter {
                    val studentLoginMatches = it.studentLogin == studentAttendance.studentLogin
                    val startTimeMatches = it.startTime == studentAttendance.startTime

                    return@filter !studentLoginMatches || !startTimeMatches
                }
                .union(listOf(studentAttendance))
                .toList()

        studentsAttendancesDao.writeValue(StudentsAttendancesModel(res))
    }
}