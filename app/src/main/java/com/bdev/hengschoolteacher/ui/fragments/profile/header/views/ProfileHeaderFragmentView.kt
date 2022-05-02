package com.bdev.hengschoolteacher.ui.fragments.profile.header.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.NavGraphDirections
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.profile.header.ProfileHeaderFragmentData
import com.bdev.hengschoolteacher.ui.fragments.profile.header.data.ProfileHeaderFragmentItem
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import kotlinx.android.synthetic.main.view_profile_header.view.*

class ProfileHeaderFragmentView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    init {
        View.inflate(context, R.layout.view_profile_header, this)
    }

    fun bind(
        data: ProfileHeaderFragmentData,
        navCommandHandler: (NavCommand) -> Unit
    ) {
        profileHeaderMyLessonsView.bind(
            active = data.currentItem == ProfileHeaderFragmentItem.LESSONS,
            hasAlert = data.hasLessonsAlert,
            clickAction = {
                navCommandHandler(
                    NavCommand.top(
                        navDir = NavGraphDirections.profileHeaderToProfileLessons()
                    )
                )
            }
        )

        profileHeaderMySalaryView.bind(
            active = data.currentItem == ProfileHeaderFragmentItem.SALARY,
            hasAlert = false,
            clickAction = {
                navCommandHandler(
                    NavCommand.top(
                        navDir = NavGraphDirections.profileHeaderToProfileSalary()
                    )
                )
            }
        )

        profileHeaderMyPaymentsView.bind(
            active = data.currentItem == ProfileHeaderFragmentItem.PAYMENTS,
            hasAlert = data.hasPaymentsAlert,
            clickAction = {
                navCommandHandler(
                    NavCommand.top(
                        navDir = NavGraphDirections.profileHeaderToProfilePayments()
                    )
                )
            }
        )

        profileHeaderDebtsView.bind(
            active = data.currentItem == ProfileHeaderFragmentItem.DEBTS,
            hasAlert = data.hasDebtsAlert,
            clickAction = {
                navCommandHandler(
                    NavCommand.top(
                        navDir = NavGraphDirections.profileHeaderToProfileDebts()
                    )
                )
            }
        )
    }
}

