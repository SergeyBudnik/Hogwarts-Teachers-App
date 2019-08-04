package com.bdev.hengschoolteacher.ui.views.app.profile

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.alerts.profile.AlertsProfileService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.profile.ProfileLessonsActivity
import com.bdev.hengschoolteacher.ui.activities.profile.ProfilePaymentsActivity
import com.bdev.hengschoolteacher.ui.activities.profile.ProfileSalaryActivity
import kotlinx.android.synthetic.main.view_profile_header.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_profile_header)
open class ProfileHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    enum class Item {
        LESSONS, SALARY, PAYMENTS;
    }

    @Bean
    lateinit var alertsProfileService: AlertsProfileService

    fun bind(currentItem: Item) {
        profileHeaderMyLessonsView.let {
            it.setActive(currentItem == Item.LESSONS)
            it.setHasAlert(alertsProfileService.haveLessonsAlerts())
            it.setOnClickListener { ProfileLessonsActivity.redirectToSibling(context as BaseActivity) }
        }

        profileHeaderMySalaryView.let {
            it.setActive(currentItem == Item.SALARY)
            it.setHasAlert(false)
            it.setOnClickListener { ProfileSalaryActivity.redirectToSibling(context as BaseActivity) }
        }

        profileHeaderMyPaymentsView.let {
            it.setActive(currentItem == Item.PAYMENTS)
            it.setHasAlert(alertsProfileService.havePaymentsAlerts())
            it.setOnClickListener { ProfilePaymentsActivity.redirectToSibling(context as BaseActivity) }
        }
    }
}

