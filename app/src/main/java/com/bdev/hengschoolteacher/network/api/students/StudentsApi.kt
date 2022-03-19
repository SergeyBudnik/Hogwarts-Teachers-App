package com.bdev.hengschoolteacher.network.api.students

import com.bdev.hengschoolteacher.data.school.student.Student
import retrofit2.Call
import retrofit2.http.GET

interface StudentsApi {
    @GET("admin/students/management")
    fun getAllStudents(): Call<List<Student>>
}
