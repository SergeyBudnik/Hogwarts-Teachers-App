package com.bdev.hengschoolteacher.data.school.teacher

import java.io.Serializable

class Teacher(
        var id: Long,
        var login: String,
        var name: String,
        var type: Type,
        var phones: List<String>,
        var emails: List<String>
) : Serializable {
    enum class Type {
        NON_NATIVE, NATIVE
    }

    @Suppress("UNUSED")
    constructor(): this(0, "", "", Type.NON_NATIVE, emptyList(), emptyList())
}
