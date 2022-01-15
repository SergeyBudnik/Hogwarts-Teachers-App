package com.bdev.hengschoolteacher.ui.views.app.profile

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.profile.ProfileDebtsActivity
import com.bdev.hengschoolteacher.ui.activities.profile.ProfileLessonsActivity
import com.bdev.hengschoolteacher.ui.activities.profile.ProfilePaymentsActivity
import com.bdev.hengschoolteacher.ui.activities.profile.ProfileSalaryActivity
import kotlinx.android.synthetic.main.view_profile_header.view.*

open class ProfileHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    enum class Item {
        LESSONS, SALARY, PAYMENTS, DEBTS;
    }

    init {
        View.inflate(context, R.layout.view_profile_header, this)
    }

    fun bind(
            currentItem: Item,
            hasLessonsAlert: Boolean, hasPaymentsAlert: Boolean, hasDebtsAlert: Boolean
    ) {
        val activity = context as BaseActivity

        profileHeaderMyLessonsView.bind(
            active = currentItem == Item.LESSONS,
            hasAlert = hasLessonsAlert,
            clickAction = { ProfileLessonsActivity.redirectToSibling(current = activity) }
        )

        profileHeaderMySalaryView.bind(
            active = currentItem == Item.SALARY,
            hasAlert = false,
            clickAction = { ProfileSalaryActivity.redirectToSibling(current = activity) }
        )

        profileHeaderMyPaymentsView.bind(
            active = currentItem == Item.PAYMENTS,
            hasAlert = hasPaymentsAlert,
            clickAction = { ProfilePaymentsActivity.redirectToSibling(current = activity) }
        )

        profileHeaderDebtsView.bind(
            active = currentItem == Item.DEBTS,
            hasAlert = hasDebtsAlert,
            clickAction = { ProfileDebtsActivity.redirectToSibling(from = activity) }
        )
    }
}

