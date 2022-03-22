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
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.loading.LoadingPageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.lessons.MonitoringLessonsPageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.profile.lessons.ProfileLessonsPageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.settings.SettingsPageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.teachers.TeachersListPageFragment
import com.bdev.hengschoolteacher.ui.resources.AppResources
import com.bdev.hengschoolteacher.ui.utils.VersionUtils
import kotlinx.android.synthetic.main.view_app_menu.view.*
import kotlinx.android.synthetic.main.view_app_menu_row.view.*

class AppMenuRowView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private val itemName: String
    private val itemIcon: Int

    init {
        View.inflate(context, R.layout.view_app_menu_row, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.AppMenuRowView, 0, 0)

        try {
            itemIcon = ta.getResourceId(R.styleable.AppMenuRowView_menuItemIcon, 0)
            itemName = ta.getString(R.styleable.AppMenuRowView_menuItemName) ?: ""
        } finally {
            ta.recycle()
        }

        appMenuItemIconView.setImageDrawable(
                AppResources.getDrawable(
                        context = context,
                        drawableId = itemIcon
                )
        )

        appMenuItemNameView.text = itemName
    }

    fun bind(isCurrent: Boolean, hasAlerts: Boolean) {
        initActiveMarkerColor(isCurrent = isCurrent, hasAlerts = hasAlerts)
        initIconColor(isCurrent = isCurrent, hasAlerts = hasAlerts)
        initNameColor(isCurrent = isCurrent, hasAlerts = hasAlerts)
    }

    private fun initActiveMarkerColor(isCurrent: Boolean, hasAlerts: Boolean) {
        appMenuItemActiveMarkerView.setBackgroundColor(
                AppResources.getColor(
                        context = context,
                        colorId = when {
                            isCurrent -> {
                                if (hasAlerts) {
                                    R.color.fill_text_basic_negative
                                } else {
                                    R.color.fill_text_basic_accent
                                }
                            }
                            else -> {
                                R.color.transparent
                            }
                        }
                )
        )
    }

    private fun initIconColor(isCurrent: Boolean, hasAlerts: Boolean) {
        appMenuItemIconView.setColorFilter(
                AppResources.getColor(
                        context = context,
                        colorId = when {
                            isCurrent -> {
                                R.color.fill_text_basic_accent
                            }
                            hasAlerts -> {
                                R.color.fill_text_basic_negative
                            }
                            else -> {
                                R.color.fill_text_basic
                            }
                        }
                ),
                PorterDuff.Mode.SRC_IN
        )
    }

    private fun initNameColor(isCurrent: Boolean, hasAlerts: Boolean) {
        appMenuItemNameView.setTextColor(
                AppResources.getColor(
                        context = context,
                        colorId = when {
                            isCurrent -> {
                                R.color.fill_text_basic_accent
                            }
                            hasAlerts -> {
                                R.color.fill_text_basic_negative
                            }
                            else -> {
                                R.color.fill_text_basic
                            }
                        }
                )
        )
    }
}

class AppMenuView : LinearLayout {
    enum class Item {
        MY_PROFILE, STUDENTS, TEACHERS, MONITORING, SETTINGS, NONE
    }

//    @Bean
//    lateinit var authService: AuthService
//
//    @Bean
//    lateinit var profileService: ProfileService
//
//    @Bean
//    lateinit var alertsProfileService: AlertsProfileService
//    @Bean
//    lateinit var alertsMonitoringSevice: AlertsMonitoringService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        View.inflate(context, R.layout.view_app_menu, this)

        // val activity = context as BasePageFragment

        menuItemMyProfileView.setOnClickListener {
        //    ProfileLessonsPageFragment.redirectToSibling(activity)
        }
        menuItemStudentsView.setOnClickListener {
        //    StudentsListPageFragment.redirectToSibling(activity)
        }
        menuItemTeachersView.setOnClickListener {
        //    TeachersListPageFragment.redirectToSibling(activity)
        }
        menuItemMonitoringView.setOnClickListener {
        //    MonitoringLessonsPageFragment.redirectToSibling(activity)
        }
        menuItemSettingsView.setOnClickListener {
        //    SettingsPageFragment.redirectToSibling(activity)
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

    fun bind(me: StaffMember?, hasProfileAlerts: Boolean, hasMonitoringAlerts: Boolean, item: Item) {
        // val me = profileService.getMe()

        teacherNameView.text = me?.person?.name ?: ""
        teacherLoginView.text = me?.login ?: ""

        menuItemMyProfileView.bind(
                isCurrent = item == Item.MY_PROFILE,
                hasAlerts = hasProfileAlerts // alertsProfileService.haveAlerts()
        )

        menuItemStudentsView.bind(
                isCurrent = item == Item.STUDENTS,
                hasAlerts = false
        )

        menuItemTeachersView.bind(
                isCurrent = item == Item.TEACHERS,
                hasAlerts = false
        )

        menuItemMonitoringView.bind(
                isCurrent = item == Item.MONITORING,
                hasAlerts = hasMonitoringAlerts // alertsMonitoringSevice.haveAlerts()
        )

        menuItemSettingsView.bind(
                item == Item.SETTINGS,
                hasAlerts = false
        )
    }
}
