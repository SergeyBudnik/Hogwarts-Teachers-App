package com.bdev.hengschoolteacher.ui.fragments.app_menu.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.NavGraphDirections
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.utils.VersionUtils
import kotlinx.android.synthetic.main.view_app_menu.view.*

class AppMenuView : LinearLayout {
    private var navCommandHandler: (NavCommand) -> Unit = {}

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        View.inflate(context, R.layout.view_app_menu, this)

        menuItemMyProfileView.setOnClickListener {
            navCommandHandler(
                NavCommand.top(navDir = NavGraphDirections.menuToProfileGlobalNavAction())
            )
        }

        menuItemStudentsView.setOnClickListener {
            navCommandHandler(
                NavCommand.top(navDir = NavGraphDirections.menuToStudentsGlobalNavAction())
            )
        }

        menuItemTeachersView.setOnClickListener {
            navCommandHandler(
                NavCommand.top(navDir = NavGraphDirections.menuToStaffMembersGlobalNavAction())
            )
        }

        menuItemMonitoringView.setOnClickListener {
            navCommandHandler(
                NavCommand.top(navDir = NavGraphDirections.menuToMonitoringGlobalNavAction())
            )
        }

        menuItemSettingsView.setOnClickListener {
            navCommandHandler(
                NavCommand.top(navDir = NavGraphDirections.menuToSettingsGlobalNavAction())
            )
        }

        refreshButtonView.setOnClickListener {
        //    LoadingPageFragment.redirectToSibling(activity)
        }

        versionView.text = VersionUtils().getVersion()

        menuUpdateAppView.setOnClickListener {
            // val appPackageName = activity.packageName

//            try {
//                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
//            } catch (e: ActivityNotFoundException) {
//                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
//            }
        }
    }

    fun bind(
        me: StaffMember,
        hasProfileAlerts: Boolean,
        hasMonitoringAlerts: Boolean,
        item: AppMenuItem,
        navCommandHandler: (NavCommand) -> Unit
    ) {
        this.navCommandHandler = navCommandHandler

        teacherNameView.text = me.person.name
        teacherLoginView.text = me.login

        menuItemMyProfileView.bind(
                isCurrent = item == AppMenuItem.MY_PROFILE,
                hasAlerts = hasProfileAlerts
        )

        menuItemStudentsView.bind(
                isCurrent = item == AppMenuItem.STUDENTS,
                hasAlerts = false
        )

        menuItemTeachersView.bind(
                isCurrent = item == AppMenuItem.TEACHERS,
                hasAlerts = false
        )

        menuItemMonitoringView.bind(
                isCurrent = item == AppMenuItem.MONITORING,
                hasAlerts = hasMonitoringAlerts
        )

        menuItemSettingsView.bind(
            item == AppMenuItem.SETTINGS,
            hasAlerts = false
        )
    }
}
