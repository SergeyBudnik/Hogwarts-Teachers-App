package com.bdev.hengschoolteacher.data.school.student_payment

import com.google.gson.annotations.SerializedName

data class NewStudentPayment constructor(
    @SerializedName("info") val info: StudentPaymentInfo
)

data class ExistingStudentPayment constructor(
    @SerializedName("id") val id: Long,
    @SerializedName("info") val info: StudentPaymentInfo,
    @SerializedName("processed") val processed: Boolean
)

data class StudentPaymentInfo constructor(
    @SerializedName("studentLogin") val studentLogin: String,
    @SerializedName("staffMemberLogin") val staffMemberLogin: String,
    @SerializedName("amount") val amount: Long,
    @SerializedName("time") val time: Long
)
