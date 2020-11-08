package com.bdev.hengschoolteacher.data.school.group

import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.Time
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

class Lesson @JsonCreator constructor(
        @JsonProperty("id") val id: Long,
        @JsonProperty("teacherLogin") val teacherLogin: String,
        @JsonProperty("online") val isOnline: Boolean,
        @JsonProperty("ignoreSingleStudentPricing") val ignoreSingleStudentPricing: Boolean,
        @JsonProperty("day") val day: DayOfWeek,
        @JsonProperty("startTime") val startTime: Time,
        @JsonProperty("finishTime") val finishTime: Time,
        @JsonProperty("creationTime") val creationTime: Long,
        @JsonProperty("deactivationTime") val deactivationTime: Long?
) {
    fun durationIn30m(): Int {
        return finishTime.order - startTime.order
    }
}