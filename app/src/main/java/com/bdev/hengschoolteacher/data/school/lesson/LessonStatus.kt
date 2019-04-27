package com.bdev.hengschoolteacher.data.school.lesson

import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

class LessonStatus @JsonCreator constructor(
        @JsonProperty("id") val id: Long?,
        @JsonProperty("lessonId") val lessonId: Long,
        @JsonProperty("type") val type: Type,
        @JsonProperty("actionTime") val actionTime: Long,
        @JsonProperty("creationTime") val creationTime: Long
) {
    enum class Type {
        CANCELED, MOVED, FINISHED
    }

    fun withId(id: Long): LessonStatus {
        return LessonStatus(
                id = id,
                lessonId = this.lessonId,
                type = this.type,
                actionTime = this.actionTime,
                creationTime = this.creationTime
        )
    }
}
