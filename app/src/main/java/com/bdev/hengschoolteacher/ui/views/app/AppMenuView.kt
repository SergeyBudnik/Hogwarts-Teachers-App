package com.bdev.hengschoolteacher.ui.views.app

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.AuthService
import com.bdev.hengschoolteacher.service.alerts.monitoring.AlertsMonitoringService
import com.bdev.hengschoolteacher.service.alerts.profile.AlertsProfileService
import com.bdev.hengschoolteacher.service.profile.ProfileService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.LoadingActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringLessonsActivity
import com.bdev.hengschoolteacher.ui.activities.profile.ProfileLessonsActivity
import com.bdev.hengschoolteacher.ui.activities.settings.SettingsActivity
import com.bdev.hengschoolteacher.ui.activities.students.StudentsListActivity
import com.bdev.hengschoolteacher.ui.activities.teachers.TeachersListActivity
import com.bdev.hengschoolteacher.ui.utils.VersionUtils
import kotlinx.android.synthetic.main.view_app_menu.view.*
import kotlinx.android.synthetic.main.view_app_menu_row.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_app_menu_row)
open class AppMenuRowView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private val itemName: String
    private val itemIcon: Int

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.AppMenuRowView, 0, 0)

        try {
            itemIcon = ta.getResourceId(R.styleable.AppMenuRowView_menuItemIcon, 0)
            itemName = ta.getString(R.styleable.AppMenuRowView_menuItemName) ?: ""
        } finally {
            ta.recycle()
        }
    }

    @AfterViews
    fun init() {
        menuItemIconView.setImageDrawable(resources.getDrawable(itemIcon))
        menuItemNameView.text = itemName
    }

    fun setCurrentItem(isCurrent: Boolean) {
        val textColor = if (isCurrent) { R.color.fill_text_basic } else { R.color.fill_text_basic_action_link }

        menuItemIconView.setColorFilter(context.resources.getColor(textColor), PorterDuff.Mode.SRC_IN)
        menuItemNameView.setTextColor(context.resources.getColor(textColor))
    }

    fun setHasAlerts(hasAlerts: Boolean) {
        menuItemAlertView.visibility = if (hasAlerts) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}

@EViewGroup(R.layout.view_app_menu)
open class AppMenuView : LinearLayout {
    enum class Item {
        MY_PROFILE, STUDENTS, TEACHERS, MONITORING, SETTINGS, NONE
    }

    @Bean
    lateinit var authService: AuthService

    @Bean
    lateinit var profileService: ProfileService

    @Bean
    lateinit var alertsProfileService: AlertsProfileService
    @Bean
    lateinit var alertsMonitoringSevice: AlertsMonitoringService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    @AfterViews
    fun init() {
        val me = profileService.getMe()

        teacherNameView.text = me?.person?.name ?: ""
        teacherLoginView.text = me?.login ?: ""

        val activity = context as BaseActivity

        menuItemMyProfileView.setOnClickListener { ProfileLessonsActivity.redirectToSibling(activity) }
        menuItemStudentsView.setOnClickListener { StudentsListActivity.redirectToSibling(activity) }
        menuItemTeachersView.setOnClickListener { TeachersListActivity.redirectToSibling(activity) }
        menuItemMonitoringView.setOnClickListener { MonitoringLessonsActivity.redirectToSibling(activity) }
        menuItemSettingsView.setOnClickListener { SettingsActivity.redirectToSibling(activity) }

        menuItemMyProfileView.setHasAlerts(alertsProfileService.haveAlerts())
        menuItemMonitoringView.setHasAlerts(alertsMonitoringSevice.haveAlerts())

        refreshButtonView.setOnClickListener { LoadingActivity.redirectToSibling(activity) }

        versionView.text = VersionUtils().getVersion()

        menuUpdateAppView.setOnClickListener {
            val appPackageName = activity.packageName

            try {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (e: ActivityNotFoundException) {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
            }
        }

        setItemSelected(Item.NONE)
    }

    fun setItemSelected(item: Item) {
        menuItemMyProfileView.setCurrentItem(item == Item.MY_PROFILE)
        menuItemStudentsView.setCurrentItem(item == Item.STUDENTS)
        menuItemTeachersView.setCurrentItem(item == Item.TEACHERS)
        menuItemMonitoringView.setCurrentItem(item == Item.MONITORING)
        menuItemSettingsView.setCurrentItem(item == Item.SETTINGS)
    }
}
