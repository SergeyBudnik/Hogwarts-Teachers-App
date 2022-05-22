package com.bdev.hengschoolteacher.ui.page_fragments.lesson.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.lesson.info.adapters.LessonInfoStudentsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.page_fragment_lesson_info.*

@AndroidEntryPoint
class LessonInfoPageFragment : BasePageFragment<LessonInfoPageFragmentViewModel>() {
    override fun provideViewModel(): LessonInfoPageFragmentViewModel =
        ViewModelProvider(this).get(LessonInfoPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.page_fragment_lesson_info, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            doInit(data = data)
        }
    }

    private fun doInit(data: LessonInfoPageFragmentData) {
        lessonTimeView.bind(
            lesson = data.lesson,
            lessonStartTime = data.lessonStartTime,
            teacherName = data.teacherName,
            teacherSurname = data.teacherSurname
        )

        lessonMarkStatusView.init(
            lessonStatusType = data.lessonStatus?.type,
            clickAction = {
                fragmentViewModel.goToLessonStatus()
            }
        )

        fillLessons(data)
    }

    private fun fillLessons(data: LessonInfoPageFragmentData) {
        val adapter = LessonInfoStudentsListAdapter(
            context = requireContext(),
            lesson = data.lesson,
            weekIndex = data.weekIndex,
            goToStudentInformationAction = { student ->
                fragmentViewModel.goToStudentInformation(studentLogin = student.login)
            },
            goToStudentPaymentAction = { student ->
                fragmentViewModel.goToStudentPayments(studentLogin = student.login)
            },
            goToLessonAttendanceAction = { a ->
                fragmentViewModel.goToLessonAttendance(studentLogin = a.studentLogin)
            }
        )

        adapter.setItems(data.students)

        lessonStudentsListView.adapter = adapter
    }
}
