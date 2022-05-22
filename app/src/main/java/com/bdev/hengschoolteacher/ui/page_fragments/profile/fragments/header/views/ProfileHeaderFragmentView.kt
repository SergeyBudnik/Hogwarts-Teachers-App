package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.header.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragmentData
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.views.CommonContentHeaderView
import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab
import kotlinx.android.synthetic.main.view_profile_header.view.*

class ProfileHeaderFragmentView(
    context: Context,
    attrs: AttributeSet
) : LinearLayout(context, attrs), CommonContentHeaderView<ProfilePageFragmentTab> {
    init {
        View.inflate(context, R.layout.view_profile_header, this)
    }

    override fun bind(
        data: CommonContentHeaderFragmentData<ProfilePageFragmentTab>,
        tabClickedAction: (ProfilePageFragmentTab) -> Unit
    ) {
        listOf(
            Pair(profileHeaderMyLessonsView, ProfilePageFragmentTab.LESSONS),
            Pair(profileHeaderMySalaryView, ProfilePageFragmentTab.SALARY),
            Pair(profileHeaderMyPaymentsView, ProfilePageFragmentTab.PAYMENTS),
            Pair(profileHeaderDebtsView, ProfilePageFragmentTab.DEBTS)
        ).forEach { (view, tab) ->
            view.bind(
                active = data.tab == tab,
                hasAlert = data.tabAlerts[tab] ?: false,
                clickAction = { tabClickedAction(tab) }
            )
        }
    }
}

