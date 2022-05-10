package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.header.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.NavGraphDirections
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.header.ProfileHeaderFragmentData
import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.views.branded.BrandedSecondaryHeaderItem
import kotlinx.android.synthetic.main.view_profile_header.view.*

class ProfileHeaderFragmentView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    init {
        View.inflate(context, R.layout.view_profile_header, this)
    }

    fun bind(
        data: ProfileHeaderFragmentData,
        tabClickedAction: (ProfilePageFragmentTab) -> Unit
    ) {
        profileHeaderMyLessonsView.bind(
            active = data.tab == ProfilePageFragmentTab.LESSONS,
            hasAlert = data.hasLessonsAlert,
            clickAction = { tabClickedAction(ProfilePageFragmentTab.LESSONS) }
        )

        profileHeaderMySalaryView.bind(
            active = data.tab == ProfilePageFragmentTab.SALARY,
            hasAlert = false,
            clickAction = { tabClickedAction(ProfilePageFragmentTab.SALARY) }
        )

        profileHeaderMyPaymentsView.bind(
            active = data.tab == ProfilePageFragmentTab.PAYMENTS,
            hasAlert = data.hasPaymentsAlert,
            clickAction = { tabClickedAction(ProfilePageFragmentTab.PAYMENTS) }
        )

        profileHeaderDebtsView.bind(
            active = data.tab == ProfilePageFragmentTab.DEBTS,
            hasAlert = data.hasDebtsAlert,
            clickAction = { tabClickedAction(ProfilePageFragmentTab.DEBTS) }
        )
    }
}

