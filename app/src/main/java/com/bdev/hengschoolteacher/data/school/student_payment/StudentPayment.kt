package com.bdev.hengschoolteacher.data.school.student_payment

import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

class StudentPayment @JsonCreator constructor(
    @JsonProperty("id") val id: Long,
    @JsonProperty("amount") amount: Long,
    @JsonProperty("studentId") studentId: Long,
    @JsonProperty("teacherId") teacherId: Long,
    @JsonProperty("time") time: Long,
    @JsonProperty("processed") processed: Boolean
) : StudentPaymentInfo(amount, studentId, teacherId, time, processed)
