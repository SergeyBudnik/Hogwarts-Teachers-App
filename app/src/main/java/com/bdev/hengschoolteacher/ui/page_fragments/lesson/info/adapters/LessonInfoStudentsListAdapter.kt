package com.bdev.hengschoolteacher.ui.page_fragments.lesson.info.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.page_fragments.lesson.info.data.LessonInfoStudent
import com.bdev.hengschoolteacher.ui.page_fragments.lesson.info.views.LessonInfoStudentItemView

class LessonInfoStudentsListAdapter(
    context: Context,
    private val goToStudentInformationAction: (String) -> Unit,
    private val goToStudentPaymentAction: (String) -> Unit,
    private val goToLessonAttendanceAction: (String) -> Unit
) : BaseItemsListAdapter<LessonInfoStudent>(context) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View =
        getItem(position).let { student ->
            if (convertView == null) {
                LessonInfoStudentItemView(context)
            } else {
                convertView as LessonInfoStudentItemView
            }.bind(
                login = student.login,
                name = student.name,
                attendanceType = student.attendanceType,
                currentDebt = student.currentDebt,
                expectedDebt = student.expectedDebt,
                goToStudentInformationAction = goToStudentInformationAction,
                goToStudentPaymentAction = goToStudentPaymentAction,
                goToLessonAttendanceAction = goToLessonAttendanceAction
            )
        }
}