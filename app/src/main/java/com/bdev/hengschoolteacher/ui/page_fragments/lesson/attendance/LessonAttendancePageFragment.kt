package com.bdev.hengschoolteacher.ui.page_fragments.lesson.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.lesson.attendance.views.LessonAttendanceButtonView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.page_fragment_lesson_student_attendance.*

@AndroidEntryPoint
class LessonAttendancePageFragment : BasePageFragment<LessonAttendancePageFragmentViewModel>() {
    override fun provideViewModel(): LessonAttendancePageFragmentViewModel =
        ViewModelProvider(this).get(LessonAttendancePageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.page_fragment_lesson_student_attendance, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            lessonStudentAttendanceLessonTimeView.bind(
                lesson = data.lesson,
                lessonStartTime = data.lessonStartTime,
                teacherName = data.teacherName,
                teacherSurname = data.teacherSurname
            )

            lessonStudentAttendanceStudentInfoView.bind(
                student = data.student
            )

            initButtons(
                actualAttendanceType = data.attendance,
                progressAttendanceType = data.progressAttendance
            )
        }

        lessonAttendanceHeaderView.setLeftButtonAction {
            fragmentViewModel.goBack()
        }
    }

    private fun initButtons(
        actualAttendanceType: StudentAttendanceType?,
        progressAttendanceType: StudentAttendanceType?
    ) {
        data class ButtonInfo(
            val view: LessonAttendanceButtonView,
            val attendanceType: StudentAttendanceType
        )

        listOf(
            ButtonInfo(view = lessonAttendanceVisitButtonView, attendanceType = StudentAttendanceType.VISITED),
            ButtonInfo(view = lessonAttendanceValidSkipButtonView, attendanceType = StudentAttendanceType.VALID_SKIP),
            ButtonInfo(view = lessonAttendanceInvalidSkipButtonView, attendanceType = StudentAttendanceType.INVALID_SKIP),
            ButtonInfo(view = lessonAttendanceFreeLessonButtonView, attendanceType = StudentAttendanceType.FREE_LESSON)
        ).forEach { buttonInfo ->
            buttonInfo.view.init(
                buttonAttendanceType = buttonInfo.attendanceType,
                actualAttendanceType = actualAttendanceType,
                progressAttendanceType = progressAttendanceType,
                clickAction = {
                    fragmentViewModel.markAttendance(attendanceType = buttonInfo.attendanceType)
                }
            )
        }
    }
}
