package com.bdev.hengschoolteacher.data.school.group

import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.Time
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

class Lesson @JsonCreator constructor(
        @JsonProperty("id") val id: Long,
        @JsonProperty("teacherId") val teacherId: Long,
        @JsonProperty("day") val day: DayOfWeek,
        @JsonProperty("startTime") val startTime: Time,
        @JsonProperty("finishTime") val finishTime: Time,
        @JsonProperty("creationTime") val creationTime: Long,
        @JsonProperty("deactivationTime") val deactivationTime: Long?
)