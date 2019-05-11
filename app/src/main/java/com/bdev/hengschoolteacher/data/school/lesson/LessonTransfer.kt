package com.bdev.hengschoolteacher.data.school.lesson

import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

class LessonTransfer @JsonCreator constructor(
        @JsonProperty("id") val id: Long?,
        @JsonProperty("lessonId") val lessonId: Long,
        @JsonProperty("teacherId") val teacherId: Long,
        @JsonProperty("fromTime") val fromTime: Long,
        @JsonProperty("toTime") val toTime: Long,
        @JsonProperty("lessonLength") val lessonLength: Long,
        @JsonProperty("reason") val reason: String
)
