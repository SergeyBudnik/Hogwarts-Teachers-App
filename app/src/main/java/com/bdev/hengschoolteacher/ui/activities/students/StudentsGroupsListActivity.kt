package com.bdev.hengschoolteacher.ui.activities.students

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_students_groups.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_list_item_students_groups)
open class StudentsGroupsListItemView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(group: Group) {

    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_students_groups)
open class StudentsGroupsListActivity : BaseActivity() {
    @AfterViews
    fun init() {
        studentsGroupsHeaderView.setLeftButtonAction { studentsGroupsMenuLayoutView.openMenu() }

        studentsGroupsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.STUDENTS)
    }
}
