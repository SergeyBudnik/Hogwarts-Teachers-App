package com.bdev.hengschoolteacher.ui.page_fragments.lesson.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatusType
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.lesson.status.views.LessonStatusButtonView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.page_fragment_lesson_status.*

@AndroidEntryPoint
class LessonStatusPageFragment : BasePageFragment<LessonStatusPageFragmentViewModel>() {
    override fun provideViewModel(): LessonStatusPageFragmentViewModel =
        ViewModelProvider(this).get(LessonStatusPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.page_fragment_lesson_status, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        lessonStatusHeaderView.setLeftButtonAction {
            fragmentViewModel.goBack()
        }

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            lessonStatusLessonTimeView.bind(
                lesson = data.lesson,
                lessonStartTime = data.lessonStartTime,
                teacherName = data.teacherName,
                teacherSurname = data.teacherSurname
            )

            lessonStatusTeacherInfoView.bind(data.teacher)

            initButtons(
                actualStatusType = data.actualStatusType,
                progressStatusType = data.progressStatusType
            )
        }
    }

    private fun initButtons(
        actualStatusType: LessonStatusType?,
        progressStatusType: LessonStatusType?
    ) {
        class ButtonInfo(
            val view: LessonStatusButtonView,
            val statusType: LessonStatusType
        )

        listOf(
            ButtonInfo(view = lessonStatusPassedView, statusType = LessonStatusType.FINISHED),
            ButtonInfo(view = lessonStatusCanceledView, statusType = LessonStatusType.CANCELED)
        ).forEach { buttonInfo ->
            buttonInfo.view.init(
                buttonStatusType = buttonInfo.statusType,
                actualStatusType = actualStatusType,
                progressStatusType = progressStatusType,
                clickAction = {
                    fragmentViewModel.markStatus(statusType = buttonInfo.statusType)
                }
            )
        }
    }
}
