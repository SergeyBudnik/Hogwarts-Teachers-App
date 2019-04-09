package com.bdev.hengschoolteacher.data.school.student

import java.io.Serializable

class Student(
        var id: Long,
        var statusType: StudentStatusType,
        var studentGroups: List<StudentGroup>,
        var name: String,
        var phones: List<String>,
        var emails: List<String>,
        var age: StudentAge,
        var educationLevel: StudentEducationLevel,
        var referralSource: StudentReferralSource
) : Serializable {
    @Suppress("UNUSED")
    constructor(): this(
            0,
            StudentStatusType.STUDYING,
            emptyList(),
            "",
            emptyList(),
            emptyList(),
            StudentAge.UNKNOWN,
            StudentEducationLevel.UNKNOWN,
            StudentReferralSource.UNKNOWN
    )
}
