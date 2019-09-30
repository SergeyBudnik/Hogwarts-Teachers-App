package com.bdev.hengschoolteacher.data.school.student

enum class StudentAttendanceType(
        val isPayed: Boolean
) {
    VISITED(isPayed = true),
    VALID_SKIP(isPayed = false),
    INVALID_SKIP(isPayed = true),
    FREE_LESSON(isPayed = false)
}