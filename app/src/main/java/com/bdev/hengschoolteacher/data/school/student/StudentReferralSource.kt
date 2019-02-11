package com.bdev.hengschoolteacher.data.school.student

import android.content.Context
import com.bdev.hengschoolteacher.R

enum class StudentReferralSource(
        val order: Int,
        private val nameId: Int
) {
    UNKNOWN              (0, R.string.student_referral_source_unknown),
    LEAFLET              (1, R.string.student_referral_source_leaflet),
    AVITO                (2, R.string.student_referral_source_avito),
    REPETITORS_SITE      (3, R.string.student_referral_source_repetitors_site),
    VK_SEARCH            (4, R.string.student_referral_source_vk_search),
    VK_ADS               (5, R.string.student_referral_source_vk_ads),
    SEARCH_ENGINE        (6, R.string.student_referral_source_search_engine),
    FRIEND_RECOMMENDATION(7, R.string.student_referral_source_friend_recommendation);

    fun getName(context: Context): String {
        return context.resources.getString(nameId)
    }
}
