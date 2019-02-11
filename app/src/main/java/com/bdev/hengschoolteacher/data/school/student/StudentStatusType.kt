package com.bdev.hengschoolteacher.data.school.student

import android.content.Context
import com.bdev.hengschoolteacher.R

enum class StudentStatusType(
        val order: Int,
        private val nameId: Int
) {
    STUDYING(0, R.string.student_status_studying),
    STOPPED (1, R.string.student_status_stopped),
    LEFT    (2, R.string.student_status_left);

    fun getName(context: Context): String {
        return context.resources.getString(nameId)
    }
}