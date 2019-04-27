package com.bdev.hengschoolteacher.ui.activities.monitoring

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.teacher.Teacher
import com.bdev.hengschoolteacher.service.TeachersService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder.Companion.redirect
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_monitoring_salaries.*
import kotlinx.android.synthetic.main.view_monitoring_teachers_item.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_monitoring_teachers_item)
open class MonitoringTeachersItemView : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(teacher: Teacher): MonitoringTeachersItemView {
        monitoringTeachersItemNameView.text = teacher.name

        return this
    }
}

class MonitoringTeachersListAdapter(context: Context) : BaseItemsListAdapter<Teacher>(context) {
    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        return if (convertView == null) {
            MonitoringTeachersItemView_.build(context)
        } else {
            convertView as MonitoringTeachersItemView
        }.bind(getItem(position))
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_salaries)
open class MonitoringSalariesActivity : BaseActivity() {
    @Bean
    lateinit var teachersService: TeachersService

    @AfterViews
    fun init() {
        monitoringSalariesHeaderView.setLeftButtonAction { monitoringSalariesMenuLayoutView.openMenu() }

        monitoringSalariesMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MONITORING)

        val adapter = MonitoringTeachersListAdapter(this)

        adapter.setItems(teachersService.getAllTeachers())

        monitoringSalariesListView.adapter = adapter

        monitoringSalariesListView.setOnItemClickListener { _, _, position, _ ->
            val teacher = adapter.getItem(position)

            redirect(this)
                    .to(MonitoringTeacherSalaryActivity_::class.java)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .withExtra(MonitoringTeacherSalaryActivity.EXTRA_TEACHER_ID, teacher.id)
                    .go()
        }
    }
}
