package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.student.StudentPayment
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import java.io.Serializable

class StudentsPaymentsModel : Serializable {
    var studentsPayments: MutableList<StudentPayment> = ArrayList()
}

@EBean
open class StudentsPaymentsDao : CommonDao<StudentsPaymentsModel>() {
    @RootContext
    lateinit var rootContext: Context

    fun getPayments(): List<StudentPayment> {
        readCache()

        return getValue().studentsPayments
    }

    fun setPayments(payments: List<StudentPayment>) {
        readCache()

        getValue().studentsPayments = payments.toMutableList()

        persist()
    }

    fun addPayment(payment: StudentPayment) {
        readCache()

        getValue().studentsPayments.add(payment)

        persist()
    }

    override fun getContext(): Context {
        return rootContext
    }

    override fun getFileName(): String {
        return StudentsPaymentsDao::class.java.canonicalName
    }

    override fun newInstance(): StudentsPaymentsModel {
        return StudentsPaymentsModel()
    }
}
