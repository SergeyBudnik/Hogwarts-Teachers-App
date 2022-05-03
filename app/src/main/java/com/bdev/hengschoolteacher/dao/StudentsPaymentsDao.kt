package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StudentsPaymentsModel constructor(
    @SerializedName("studentsPayments") val studentsPayments: Map<Long, ExistingStudentPayment>
)

interface StudentsPaymentsDao : CommonDao<StudentsPaymentsModel>

class StudentsPaymentsDaoImpl @Inject constructor(
    @ApplicationContext context: Context
): StudentsPaymentsDao, CommonDaoImpl<StudentsPaymentsModel>(context = context) {
    override fun getFileName() = "students-payments.data"
    override fun newInstance(): StudentsPaymentsModel = StudentsPaymentsModel(emptyMap())
    override fun deserialize(json: String): StudentsPaymentsModel = Gson().fromJson(json, StudentsPaymentsModel::class.java)
}
