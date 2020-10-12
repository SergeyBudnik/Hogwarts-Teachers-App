package com.bdev.hengschoolteacher.data.school.teacher

import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.Time
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

enum class TeacherActionType {
    ROAD, LESSON, ONLINE_LESSON
}

class TeacherAction @JsonCreator constructor(
        @JsonProperty("type") val type: TeacherActionType,
        @JsonProperty("dayOfWeek") val dayOfWeek: DayOfWeek,
        @JsonProperty("startTime") val startTime: Time,
        @JsonProperty("finishTime") val finishTime: Time
)