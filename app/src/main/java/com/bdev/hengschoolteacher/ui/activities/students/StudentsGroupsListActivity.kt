package com.bdev.hengschoolteacher.ui.activities.students

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_students_groups.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity(R.layout.activity_students_groups)
open class StudentsGroupsListActivity : BaseActivity() {
    @AfterViews
    fun init() {
        studentsGroupsHeaderView.setLeftButtonAction { studentsGroupsMenuLayoutView.openMenu() }

        studentsGroupsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.STUDENTS)
    }
}
