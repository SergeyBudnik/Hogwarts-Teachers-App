package com.bdev.hengschoolteacher.data.school.student

import java.io.Serializable

class StudentPayment(
    var id: Long?,
    var amount: Long,
    var studentId: Long,
    var teacherId: Long,
    var time: Long,
    var processed: Boolean
) : Serializable {
    @Suppress("UNUSED")
    constructor(): this(null, 0L, 0L, 0L, 0L, false)
}
