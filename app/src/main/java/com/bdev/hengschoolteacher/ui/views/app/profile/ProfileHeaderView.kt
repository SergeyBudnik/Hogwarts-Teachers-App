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
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_profile_header)
open class ProfileHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private enum class Item(val id: Int) {
        LESSONS(1), SALARY(2), PAYMENTS(3);

        companion object {
            fun findById(id: Int): Item {
                return values().find { it.id == id } ?: throw RuntimeException()
            }
        }
    }

    @Bean
    lateinit var alertsProfileService: AlertsProfileService

    private val item: Item

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ProfileHeaderView, 0, 0)

        try {
            item = Item.findById(ta.getInt(R.styleable.ProfileHeaderView_profile_item, 1))
        } finally {
            ta.recycle()
        }
    }

    @AfterViews
    fun init() {
        profileHeaderMyLessonsView.setActive(item == Item.LESSONS)
        profileHeaderMySalaryView.setActive(item == Item.SALARY)
        profileHeaderMyPaymentsView.setActive(item == Item.PAYMENTS)

        if (alertsProfileService.haveAlerts()) {
            profileHeaderMyLessonsView.setIcon(
                    iconId = R.drawable.ic_alert,
                    colorId = R.color.fill_text_basic_negative
            )
        }

        if (alertsProfileService.havePaymentsAlerts()) {
            profileHeaderMyPaymentsView.setIcon(
                    iconId = R.drawable.ic_alert,
                    colorId = R.color.fill_text_basic_negative
            )
        }

        profileHeaderMyLessonsView.setOnClickListener {
            ProfileLessonsActivity.redirectToSibling(context as BaseActivity)
        }

        profileHeaderMySalaryView.setOnClickListener {
            ProfileSalaryActivity.redirectToSibling(context as BaseActivity)
        }

        profileHeaderMyPaymentsView.setOnClickListener {
            ProfilePaymentsActivity.redirectToSibling(context as BaseActivity)
        }
    }
}

