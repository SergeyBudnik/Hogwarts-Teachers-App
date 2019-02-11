package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.student.Student
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import java.io.Serializable

class StudentsModel : Serializable {
    var students: List<Student> = emptyList()
}

@EBean(scope = EBean.Scope.Singleton)
open class StudentsDao : CommonDao<StudentsModel>() {
    @RootContext
    lateinit var rootContext: Context

    fun setStudents(students: List<Student>) {
        readCache()

        getValue().students = students

        persist()
    }

    fun getStudents(): List<Student> {
        readCache()

        return getValue().students
    }

    override fun getContext(): Context { return rootContext }
    override fun getFileName(): String { return StudentsDao::class.java.canonicalName }
    override fun newInstance(): StudentsModel { return StudentsModel() }
}
