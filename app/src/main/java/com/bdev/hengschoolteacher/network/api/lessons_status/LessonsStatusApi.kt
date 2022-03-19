package com.bdev.hengschoolteacher.network.api.lessons_status

import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LessonsStatusApi {
    @GET("lesson-status/{from}/{to}")
    fun getAllLessonsStatuses(
        @Path(value = "from") from: Long,
        @Path(value = "to") to: Long
    ): Call<List<LessonStatus>>

    @POST("lesson-status")
    fun addLessonStatus(@Body lessonStatus: LessonStatus): Call<Long>
}
