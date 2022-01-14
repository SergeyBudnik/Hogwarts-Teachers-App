package com.bdev.hengschoolteacher.ui.activities.lesson.status

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.async.LessonStatusAsyncService
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.services.groups.GroupsStorageService
import com.bdev.hengschoolteacher.services.LessonStatusService
import com.bdev.hengschoolteacher.services.lessons.LessonsService
import com.bdev.hengschoolteacher.services.groups.GroupsStorageServiceImpl
import com.bdev.hengschoolteacher.services.staff.StaffMembersStorageService
import com.bdev.hengschoolteacher.services.teacher.TeacherInfoService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.lesson.status.LessonStatusActivityParams.EXTRA_DATA
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.branded.BrandedActionButtonView
import kotlinx.android.synthetic.main.activity_lesson.*
import kotlinx.android.synthetic.main.activity_lesson_status.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra
import java.util.*

@SuppressLint("Registered")
@EActivity(R.layout.activity_lesson_status)
open class LessonStatusActivity : BaseActivity() {
    @Bean(GroupsStorageServiceImpl::class)
    lateinit var groupsStorageService: GroupsStorageService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var lessonStatusService: LessonStatusService
    @Bean
    lateinit var lessonsStatusAsyncService: LessonStatusAsyncService
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageService
    @Bean
    lateinit var teacherInfoService: TeacherInfoService

    @Extra(EXTRA_DATA)
    lateinit var activityData: LessonStatusActivityData

    @AfterViews
    fun init() {
        lessonStatusHeaderView.setLeftButtonAction { doFinish() }

        val lesson = lessonsService.getLesson(activityData.lessonId)?.lesson ?: throw RuntimeException()

        staffMembersStorageService.getStaffMember(lesson.teacherLogin)?.let { teacher ->
            lessonStatusLessonTimeView.bind(
                    lesson = lesson,
                    lessonStartTime = lessonsService.getLessonStartTime(lesson.id, activityData.weekIndex),
                    teacherName = teacherInfoService.getTeachersName(teacher),
                    teacherSurname = teacherInfoService.getTeachersSurname(teacher)
            )
        }

        lessonStatusTeacherInfoView.bind(
                staffMembersStorageService.getStaffMember(lesson.teacherLogin)
        )

        initButtons()
    }

    private fun initButtons() {
        val status = lessonStatusService.getLessonStatus(
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
                    .onSuccess { runOnUiThread {
                        initButtons()
                        doFinish()
                    } }
                    .onAuthFail {  }
                    .onOtherFail {  }
        }
    }

    override fun onBackPressed() = LessonStatusActivityNavigation.goBackWithCancel(from = this)

    private fun doFinish() = LessonStatusActivityNavigation.goBackWithSuccess(from = this)

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
