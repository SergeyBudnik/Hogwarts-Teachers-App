package com.bdev.hengschoolteacher.ui.views.app.profile

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.services.alerts.profile.AlertsProfileService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.profile.ProfileDebtsActivity
import com.bdev.hengschoolteacher.ui.activities.profile.ProfileLessonsActivity
import com.bdev.hengschoolteacher.ui.activities.profile.ProfilePaymentsActivity
import com.bdev.hengschoolteacher.ui.activities.profile.ProfileSalaryActivity
import kotlinx.android.synthetic.main.view_profile_header.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_profile_header)
open class ProfileHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    enum class Item {
        LESSONS, SALARY, PAYMENTS, DEBTS;
    }

    @Bean
    lateinit var alertsProfileService: AlertsProfileService

    fun bind(currentItem: Item) {
        val activity = context as BaseActivity

        profileHeaderMyLessonsView.bind(
            active = currentItem == Item.LESSONS,
            hasAlert = alertsProfileService.haveLessonsAlerts(),
            clickAction = { ProfileLessonsActivity.redirectToSibling(current = activity) }
        )

        profileHeaderMySalaryView.bind(
            active = currentItem == Item.SALARY,
            hasAlert = false,
            clickAction = { ProfileSalaryActivity.redirectToSibling(current = activity) }
        )

        profileHeaderMyPaymentsView.bind(
            active = currentItem == Item.PAYMENTS,
            hasAlert = alertsProfileService.havePaymentsAlerts(),
            clickAction = { ProfilePaymentsActivity.redirectToSibling(current = activity) }
        )

        profileHeaderDebtsView.bind(
            active = currentItem == Item.DEBTS,
            hasAlert = alertsProfileService.haveDebtsAlerts(),
            clickAction = { ProfileDebtsActivity.redirectToSibling(from = activity) }
        )
    }
}

