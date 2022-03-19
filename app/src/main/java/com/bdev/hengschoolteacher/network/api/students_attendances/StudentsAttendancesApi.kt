package com.bdev.hengschoolteacher.network.api.students_attendances

import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StudentsAttendancesApi {
    @GET("admin/students/attendances")
    fun getStudentsAttendances(): Call<List<StudentAttendance>>

    @POST("admin/students/attendances")
    fun addStudentAttendance(@Body attendance: StudentAttendance): Call<Void>
}
