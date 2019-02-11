package com.bdev.hengschoolteacher.ui.activities.profile

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_profile_account.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity(R.layout.activity_profile_account)
open class ProfileAccountActivity : BaseActivity() {
    @AfterViews
    fun init() {
        profileAccountHeaderView.setLeftButtonAction { profileAccountMenuLayoutView.openMenu() }

        profileAccountMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)
    }
}
