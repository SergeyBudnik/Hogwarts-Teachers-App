package com.bdev.hengschoolteacher.data.school.education_info

import android.content.Context
import com.bdev.hengschoolteacher.R

enum class EducationLevel(
        val order: Long,
        private val nameId: Int
) {
    UNKNOWN           (0, R.string.student_education_unknown),
    BEGINNER          (1, R.string.student_education_beginner),
    ELEMENTARY        (2, R.string.student_education_elementary),
    PRE_INTERMEDIATE  (3, R.string.student_education_pre_intermediate),
    INTERMEDIATE      (4, R.string.student_education_intermediate),
    UPPER_INTERMEDIATE(5, R.string.student_education_upper_intermediate),
    PRE_ADVANCED      (6, R.string.student_education_pre_advanced),
    ADVANCED          (7, R.string.student_education_advanced);

    fun getName(context: Context): String {
        return context.resources.getString(nameId)
    }
}
