package com.bdev.hengschoolteacher.data.school.lesson

import com.google.gson.annotations.SerializedName

class LessonStatus constructor(
    @SerializedName("id") val id: Long?,
    @SerializedName("lessonId") val lessonId: Long,
    @SerializedName("type") val type: Type,
    @SerializedName("actionTime") val actionTime: Long,
    @SerializedName("creationTime") val creationTime: Long
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
