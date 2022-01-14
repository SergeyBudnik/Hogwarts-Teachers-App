package com.bdev.hengschoolteacher.ui.activities.lesson.info.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.ui.activities.lesson.attendance.LessonAttendanceActivityData
import com.bdev.hengschoolteacher.ui.activities.lesson.info.views.LessonInfoStudentItemView
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter

class LessonInfoStudentsListAdapter(
        context: Context,
        private val lesson: Lesson,
        private val weekIndex: Int,
        private val goToStudentInformationAction: (Student) -> Unit,
        private val goToStudentPaymentAction: (Student) -> Unit,
        private val goToLessonAttendanceAction: (LessonAttendanceActivityData) -> Unit
) : BaseItemsListAdapter<Pair<Pair<Student, StudentAttendanceType?>, Pair<Int, Int>>>(context) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView == null) {
            LessonInfoStudentItemView(context)
        } else {
            convertView as LessonInfoStudentItemView
        }.bind(
                student = getItem(position).first.first,
                lesson = lesson,
                weekIndex = weekIndex,
                attendanceType = getItem(position).first.second,
                currentDebt = getItem(position).second.first,
                expectedDebt = getItem(position).second.second,
                goToStudentInformationAction = goToStudentInformationAction,
                goToStudentPaymentAction = goToStudentPaymentAction,
                goToLessonAttendanceAction = goToLessonAttendanceAction
        )
    }
}