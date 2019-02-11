package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.teacher.Teacher
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import java.io.Serializable

class TeachersModel : Serializable {
    var teachers: List<Teacher> = emptyList()
}

@EBean(scope = EBean.Scope.Singleton)
open class TeachersDao : CommonDao<TeachersModel>() {
    @RootContext
    lateinit var rootContext: Context

    fun getTeachers(): List<Teacher> {
        readCache()

        return getValue().teachers
    }

    fun setTeachers(teachers: List<Teacher>) {
        readCache()

        getValue().teachers = teachers

        persist()
    }

    override fun getContext(): Context {
        return rootContext
    }

    override fun getFileName(): String {
        return TeachersDao::class.java.canonicalName
    }

    override fun newInstance(): TeachersModel {
        return TeachersModel()
    }
}
