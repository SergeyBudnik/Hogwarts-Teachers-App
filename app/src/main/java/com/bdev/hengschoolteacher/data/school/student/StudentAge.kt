package com.bdev.hengschoolteacher.data.school.student

import android.content.Context
import com.bdev.hengschoolteacher.R

enum class StudentAge(
        val order: Int,
        private val nameId: Int
) {
    UNKNOWN     (0, R.string.student_age_unknown),
    PRE_SCHOOL  (1, R.string.student_age_pre_school),
    SCHOOL_1_3  (2, R.string.student_age_school_1_3),
    SCHOOL_5_7  (3, R.string.student_age_school_5_7),
    SCHOOL_8_9  (4, R.string.student_age_school_8_9),
    SCHOOL_10_11(5, R.string.student_age_school_10_11),
    ADULT       (6, R.string.student_age_adult),
    SENIOR      (7, R.string.student_age_senior);

    fun getName(context: Context): String {
        return context.resources.getString(nameId)
    }
}
