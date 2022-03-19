package com.bdev.hengschoolteacher.network.api.students_payments

import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import com.bdev.hengschoolteacher.data.school.student_payment.NewStudentPayment
import retrofit2.Call
import retrofit2.http.*

interface StudentsPaymentsApi {
    @GET("admin/students/payments")
    fun getStudentsPayments(): Call<List<ExistingStudentPayment>>

    @POST("admin/students/payments")
    fun addStudentPayment(@Body paymentNew: NewStudentPayment): Call<Long?>

    @PUT("admin/students/payments/{paymentId}/processed/{processed}")
    fun setStudentPaymentsProcessed(
        @Path(value = "paymentId") paymentId: Long,
        @Path(value = "processed") processed: Boolean
    ): Call<Void>
}
