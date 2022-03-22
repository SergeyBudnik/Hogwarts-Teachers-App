package com.bdev.hengschoolteacher.ui.views.app.profile

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.profile.debts.ProfileDebtsPageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.profile.lessons.ProfileLessonsPageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.profile.payments.ProfilePaymentsPageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.profile.salary.ProfileSalaryPageFragment
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
        // val activity = context as BasePageFragment

        profileHeaderMyLessonsView.bind(
            active = currentItem == Item.LESSONS,
            hasAlert = hasLessonsAlert,
            clickAction = {
            //    ProfileLessonsPageFragment.redirectToSibling(current = activity)
            }
        )

        profileHeaderMySalaryView.bind(
            active = currentItem == Item.SALARY,
            hasAlert = false,
            clickAction = {
            //    ProfileSalaryPageFragment.redirectToSibling(current = activity)
            }
        )

        profileHeaderMyPaymentsView.bind(
            active = currentItem == Item.PAYMENTS,
            hasAlert = hasPaymentsAlert,
            clickAction = {
            //    ProfilePaymentsPageFragment.redirectToSibling(current = activity)
            }
        )

        profileHeaderDebtsView.bind(
            active = currentItem == Item.DEBTS,
            hasAlert = hasDebtsAlert,
            clickAction = {
            //    ProfileDebtsPageFragment.redirectToSibling(from = activity)
            }
        )
    }
}

