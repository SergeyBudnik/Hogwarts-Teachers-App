package com.bdev.hengschoolteacher.data.school.student_payment

import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

data class StudentPaymentInfo @JsonCreator constructor(
        @JsonProperty("amount") val amount: Long,
        @JsonProperty("studentId") val studentId: Long,
        @JsonProperty("staffMemberLogin") val staffMemberLogin: String,
        @JsonProperty("time") val time: Long,
        @JsonProperty("processed") val processed: Boolean
)
