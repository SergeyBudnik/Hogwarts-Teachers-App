package com.bdev.hengschoolteacher.data.school.student

import com.bdev.hengschoolteacher.data.school.group.GroupType
import java.io.Serializable

class StudentAttendance(
        var id: Long?,
        var studentId: Long,
        var groupType: GroupType,
        var studentsInGroup: Int,
        var startTime: Long,
        var finishTime: Long,
        var type: Type
) : Serializable {
    enum class Type {
        VISITED, VALID_SKIP, INVALID_SKIP
    }

    @Suppress("UNUSED")
    constructor(): this(0L, 0L, GroupType.GROUP, 0, 0L, 0L, Type.VISITED)
}
