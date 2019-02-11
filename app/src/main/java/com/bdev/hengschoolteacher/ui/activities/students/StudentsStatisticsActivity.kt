package com.bdev.hengschoolteacher.ui.activities.students

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_students_list_statistics.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity(R.layout.activity_students_list_statistics)
open class StudentsStatisticsActivity : BaseActivity() {
    @Bean
    lateinit var studentsService: StudentsService

    @AfterViews
    fun init() {
        studentsStatisticsHeaderView.setLeftButtonAction { studentsStatisticsMenuLayoutView.openMenu() }

        studentsStatisticsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.STUDENTS)

        studentsListStatisticsAmountOfStudentsView.text = "${studentsService.getAllStudents().size}"
    }
}
