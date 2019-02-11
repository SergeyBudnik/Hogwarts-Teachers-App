package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import java.io.Serializable

class StudentsAttendancesModel : Serializable {
    var studentsAttendances: MutableList<StudentAttendance> = ArrayList()
}

@EBean(scope = EBean.Scope.Singleton)
open class StudentsAttendancesDao : CommonDao<StudentsAttendancesModel>() {
    @RootContext
    lateinit var rootContext: Context

    fun getAttendances(): List<StudentAttendance> {
        readCache()

        return getValue().studentsAttendances
    }

    fun setAttendances(attendances: List<StudentAttendance>) {
        readCache()

        getValue().studentsAttendances = attendances.toMutableList()

        persist()
    }

    fun addAttendance(attendance: StudentAttendance) {
        readCache()

        getValue().studentsAttendances.add(attendance)

        persist()
    }

    override fun getContext(): Context {
        return rootContext
    }

    override fun getFileName(): String {
        return StudentsAttendancesDao::class.java.canonicalName
    }

    override fun newInstance(): StudentsAttendancesModel {
        return StudentsAttendancesModel()
    }
}
