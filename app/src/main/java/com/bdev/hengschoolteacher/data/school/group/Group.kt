package com.bdev.hengschoolteacher.data.school.group

import java.io.Serializable

enum class GroupType {
    INDIVIDUAL, GROUP
}

class Group(
        var id: Long,
        var cabinetId: Long,
        var type: GroupType,
        var lessons: List<Lesson>,
        var color: String
) : Serializable {
    @Suppress("UNUSED")
    constructor(): this(0L, 0L, GroupType.GROUP, emptyList(), "#ffffff")
}