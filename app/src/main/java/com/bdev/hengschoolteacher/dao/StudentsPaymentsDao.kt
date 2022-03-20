package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import dagger.hilt.android.qualifiers.ApplicationContext
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper
import javax.inject.Inject

class StudentsPaymentsModel @JsonCreator constructor(
    @JsonProperty("studentsPayments") val studentsPayments: Map<Long, ExistingStudentPayment>
)

interface StudentsPaymentsDao : CommonDao<StudentsPaymentsModel>

class StudentsPaymentsDaoImpl @Inject constructor(
    @ApplicationContext context: Context
): StudentsPaymentsDao, CommonDaoImpl<StudentsPaymentsModel>(context = context) {
    override fun getFileName(): String {
        return "students-payments.data"
    }

    override fun newInstance(): StudentsPaymentsModel {
        return StudentsPaymentsModel(emptyMap())
    }

    override fun deserialize(json: String): StudentsPaymentsModel {
        return ObjectMapper().readValue(json, StudentsPaymentsModel::class.java)
    }
}
