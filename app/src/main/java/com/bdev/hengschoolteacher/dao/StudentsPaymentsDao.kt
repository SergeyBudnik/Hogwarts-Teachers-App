package com.bdev.hengschoolteacher.dao

import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import org.androidannotations.annotations.EBean
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper

class StudentsPaymentsModel @JsonCreator constructor(
    @JsonProperty("studentsPayments") val studentsPayments: Map<Long, ExistingStudentPayment>
)

interface StudentsPaymentsDao : CommonDao<StudentsPaymentsModel>

@EBean(scope = EBean.Scope.Singleton)
open class StudentsPaymentsDaoImpl : StudentsPaymentsDao, CommonDaoImpl<StudentsPaymentsModel>() {
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
