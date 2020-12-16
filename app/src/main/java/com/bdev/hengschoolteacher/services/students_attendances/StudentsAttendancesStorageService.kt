package com.bdev.hengschoolteacher.services.students_attendances

import com.bdev.hengschoolteacher.dao.StudentsAttendancesDao
import com.bdev.hengschoolteacher.dao.StudentsAttendancesModel
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean(scope = EBean.Scope.Singleton)
open class StudentsAttendancesStorageService {
    @Bean
    lateinit var studentsAttendancesDao: StudentsAttendancesDao

    fun getAll(): List<StudentAttendance> {
        return studentsAttendancesDao
                .readValue()
                .studentsAttendances
    }

    fun setAll(studentsAttendances: List<StudentAttendance>) {
        studentsAttendancesDao.writeValue(
                StudentsAttendancesModel(studentsAttendances = studentsAttendances)
        )
    }

    fun add(studentAttendance: StudentAttendance) {
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