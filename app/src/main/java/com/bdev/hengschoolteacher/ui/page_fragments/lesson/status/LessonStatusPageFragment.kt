package com.bdev.hengschoolteacher.ui.page_fragments.lesson.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.interactors.groups.GroupsStorageInteractor
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusLoadingInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusStorageInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.teachers.TeacherInfoInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.views.app.root.HtPageRootView
import com.bdev.hengschoolteacher.ui.views.branded.BrandedActionButtonView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_lesson_status.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LessonStatusPageFragment : BasePageFragment<LessonStatusPageFragmentViewModel>() {
    @Inject lateinit var groupsStorageInteractor: GroupsStorageInteractor
    @Inject lateinit var lessonsService: LessonsInteractor
    @Inject lateinit var lessonsStatusService: LessonsStatusStorageInteractor
    @Inject lateinit var lessonsStatusAsyncService: LessonsStatusLoadingInteractor
    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor
    @Inject lateinit var teacherInfoInteractor: TeacherInfoInteractor

    lateinit var activityData: LessonStatusActivityData

    override fun provideViewModel(): LessonStatusPageFragmentViewModel =
        ViewModelProvider(this).get(LessonStatusPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_lesson_status, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        lessonStatusHeaderView.setLeftButtonAction { doFinish() }

        val lesson = lessonsService.getLesson(activityData.lessonId)?.lesson ?: throw RuntimeException()

        staffMembersStorageInteractor.getStaffMember(lesson.teacherLogin)?.let { teacher ->
            lessonStatusLessonTimeView.bind(
                    lesson = lesson,
                    lessonStartTime = lessonsService.getLessonStartTime(lesson.id, activityData.weekIndex),
                    teacherName = teacherInfoInteractor.getTeachersName(teacher),
                    teacherSurname = teacherInfoInteractor.getTeachersSurname(teacher)
            )
        }

        lessonStatusTeacherInfoView.bind(
                staffMembersStorageInteractor.getStaffMember(lesson.teacherLogin)
        )

        initButtons()
    }

    private fun initButtons() {
        val status = lessonsStatusService.getLessonStatus(
                activityData.lessonId,
                lessonsService.getLessonStartTime(
                        activityData.lessonId,
                        activityData.weekIndex
                )
        )

        initButton(
                lessonStatusPassedView,
                listOf(lessonStatusPassedView, lessonStatusCanceledView),
                status?.type,
                LessonStatus.Type.FINISHED,
                "Проведено",
                R.color.fill_text_basic_positive
        )

        initButton(
                lessonStatusCanceledView,
                listOf(lessonStatusPassedView, lessonStatusCanceledView),
                status?.type,
                LessonStatus.Type.CANCELED,
                "Отменено",
                R.color.fill_text_basic_negative
        )
    }

    private fun initButton(
            buttonView: BrandedActionButtonView,
            allButtonsViews: List<BrandedActionButtonView>,
            actualStatus: LessonStatus.Type?,
            buttonStatus: LessonStatus.Type,
            text: String,
            buttonColorId: Int
    ) {
        buttonView.setButtonText(text)

        if (actualStatus != null) {
            buttonView.setButtonIcon(
                    R.drawable.ic_ok,
                    if (actualStatus == buttonStatus) { buttonColorId } else { R.color.fill_text_basic_light }
            )
        }

        buttonView.setOnClickListener {
            allButtonsViews.forEach { btn ->
                btn.hideButtonIcon()
                btn.setOnClickListener { }
            }

            buttonView.setButtonInProgressIcon()

            lessonsStatusAsyncService.addLessonStatus(LessonStatus(
                    null,
                    activityData.lessonId,
                    buttonStatus,
                    lessonsService.getLessonStartTime(
                            activityData.lessonId,
                            activityData.weekIndex
                    ),
                    Date().time
            ))
                    .onSuccess {
                        initButtons()
                        doFinish()
                    }
                    .onAuthFail {  }
                    .onOtherFail {  }
        }
    }

    private fun doFinish() = LessonStatusActivityNavigation.goBackWithSuccess(from = this)
}
