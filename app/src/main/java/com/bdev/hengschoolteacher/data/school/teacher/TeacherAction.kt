package com.bdev.hengschoolteacher.data.school.teacher

import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.Time
import com.google.gson.annotations.SerializedName

enum class TeacherActionType {
    ROAD, LESSON, ONLINE_LESSON
}

class TeacherAction constructor(
    @SerializedName("type") val type: TeacherActionType,
    @SerializedName("dayOfWeek") val dayOfWeek: DayOfWeek,
    @SerializedName("startTime") val startTime: Time,
    @SerializedName("finishTime") val finishTime: Time
)