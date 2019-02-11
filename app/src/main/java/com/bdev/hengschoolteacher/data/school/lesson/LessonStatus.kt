package com.bdev.hengschoolteacher.data.school.lesson

import java.io.Serializable

class LessonStatus(
    var id: Long?,
    var lessonId: Long,
    var type: Type,
    var actionTime: Long,
    var creationTime: Long
): Serializable {
    enum class Type {
        CANCELED, MOVED, FINISHED
    }

    @Suppress("UNUSED")
    constructor(): this(0L, 0L, Type.CANCELED, 0L, 0L)
}
