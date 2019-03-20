package com.bdev.hengschoolteacher.ui.activities.students

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.GroupType
import com.bdev.hengschoolteacher.service.GroupsService
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
    @Bean
    lateinit var groupsService: GroupsService

    @AfterViews
    fun init() {
        studentsStatisticsHeaderView.setLeftButtonAction { studentsStatisticsMenuLayoutView.openMenu() }

        studentsStatisticsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.STUDENTS)

        studentsListStatisticsAmountOfStudentsView.text = "${studentsService.getAllStudents().size}"

        studentsListStatisticsAmountOfGroupsIndividualView.text = "${getGroupsOfCertainAmountAndType(1, GroupType.INDIVIDUAL)}"

        studentsListStatisticsAmountOfGroups1View.text = "${getGroupsOfCertainAmountAndType(1, GroupType.GROUP)}"
        studentsListStatisticsAmountOfGroups2View.text = "${getGroupsOfCertainAmountAndType(2, GroupType.GROUP)}"
        studentsListStatisticsAmountOfGroups3View.text = "${getGroupsOfCertainAmountAndType(3, GroupType.GROUP)}"
        studentsListStatisticsAmountOfGroups4View.text = "${getGroupsOfCertainAmountAndType(4, GroupType.GROUP)}"
        studentsListStatisticsAmountOfGroups5View.text = "${getGroupsOfCertainAmountAndType(5, GroupType.GROUP)}"
        studentsListStatisticsAmountOfGroups6View.text = "${getGroupsOfCertainAmountAndType(6, GroupType.GROUP)}"
    }


    private fun getGroupsOfCertainAmountAndType(amount: Int, type: GroupType): Int {
        return groupsService
                .getGroups()
                .filter { it.type == type }
                .map { studentsService.getGroupStudents(it.id).size }
                .filter { it == amount }
                .size
    }
}
