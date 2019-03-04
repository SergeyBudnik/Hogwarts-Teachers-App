package com.bdev.hengschoolteacher.ui.views.app

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.AuthService
import com.bdev.hengschoolteacher.service.TeachersService
import com.bdev.hengschoolteacher.service.UserPreferencesService
import com.bdev.hengschoolteacher.ui.activities.*
import com.bdev.hengschoolteacher.ui.activities.actions.ActionsUsersRequestsListActivity_
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringLessonsActivity_
import com.bdev.hengschoolteacher.ui.activities.profile.ProfileLessonsActivity_
import com.bdev.hengschoolteacher.ui.activities.students.StudentsListActivity_
import com.bdev.hengschoolteacher.ui.activities.teachers.TeachersListActivity_
import com.bdev.hengschoolteacher.ui.utils.RedirectUtils.Companion.redirect
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
            itemName = ta.getString(R.styleable.AppMenuRowView_menuItemName)
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
        val textColor = if (isCurrent) { R.color.fill_text_base } else { R.color.fill_text_action_link }
        val backgroundColor = if (isCurrent) { R.color.alt_contrast_light } else { R.color.transparent }

        menuItemView.setBackgroundColor(context.resources.getColor(backgroundColor))

        menuItemIconView.setColorFilter(context.resources.getColor(textColor), PorterDuff.Mode.SRC_IN)
        menuItemNameView.setTextColor(context.resources.getColor(textColor))
    }
}

@EViewGroup(R.layout.view_app_menu)
open class AppMenuView : LinearLayout {
    enum class Item {
        MY_PROFILE, STUDENTS, TEACHERS, MONITORING, ACTIONS, NONE
    }

    @Bean
    lateinit var authService: AuthService

    @Bean
    lateinit var userPreferencesService: UserPreferencesService
    @Bean
    lateinit var teachersService: TeachersService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    @AfterViews
    fun init() {
        if (!isInEditMode) {
            val login = userPreferencesService.getUserLogin() ?: throw RuntimeException()
            val teacher = teachersService.getTeacherByLogin(login) ?: throw RuntimeException()

            teacherNameView.text = teacher.name
            teacherLoginView.text = login
        }

        menuItemMyProfileView.setOnClickListener { goToPage(ProfileLessonsActivity_::class.java) }
        menuItemStudentsView.setOnClickListener { goToPage(StudentsListActivity_::class.java) }
        menuItemTeachersView.setOnClickListener { goToPage(TeachersListActivity_::class.java) }
        menuItemMonitoringView.setOnClickListener { goToPage(MonitoringLessonsActivity_::class.java) }
        menuItemActionsView.setOnClickListener { goToPage(ActionsUsersRequestsListActivity_::class.java) }

        refreshButtonView.setOnClickListener { goToPage(LoadingActivity_::class.java) }

        versionView.text = VersionUtils().getVersion()

        menuUpdateAppView.setOnClickListener {
            val activity = context as BaseActivity

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
        menuItemActionsView.setCurrentItem(item == Item.ACTIONS)
    }

    private fun goToPage(target: Class<out BaseActivity>) {
        redirect(context as BaseActivity)
                .to(target)
                .withAnim(0, 0)
                .goAndCloseCurrent()
    }
}
