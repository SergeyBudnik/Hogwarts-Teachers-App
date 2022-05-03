package com.bdev.hengschoolteacher.data.school.group

import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.Time
import com.google.gson.annotations.SerializedName

class Lesson constructor(
    @SerializedName("id") val id: Long,
    @SerializedName("teacherLogin") val teacherLogin: String,
    @SerializedName("online") val isOnline: Boolean,
    @SerializedName("ignoreSingleStudentPricing") val ignoreSingleStudentPricing: Boolean,
    @SerializedName("day") val day: DayOfWeek,
    @SerializedName("startTime") val startTime: Time,
    @SerializedName("finishTime") val finishTime: Time,
    @SerializedName("creationTime") val creationTime: Long,
    @SerializedName("deactivationTime") val deactivationTime: Long
) {
    fun durationIn30m(): Int {
        return finishTime.order - startTime.order
    }
}