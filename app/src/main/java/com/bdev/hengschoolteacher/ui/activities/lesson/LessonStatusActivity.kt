package com.bdev.hengschoolteacher.ui.activities.lesson

import android.annotation.SuppressLint
import android.app.Activity
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.async.LessonStatusAsyncService
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.service.GroupsService
import com.bdev.hengschoolteacher.service.LessonStatusService
import com.bdev.hengschoolteacher.service.LessonsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.branded.BrandedActionButtonView
import kotlinx.android.synthetic.main.activity_lesson_status.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra
import java.util.*

@SuppressLint("Registered")
@EActivity(R.layout.activity_lesson_status)
open class LessonStatusActivity : BaseActivity() {
    companion object {
        const val EXTRA_LESSON_ID = "EXTRA_LESSON_ID"
        const val EXTRA_WEEK_INDEX = "EXTRA_WEEK_INDEX"

        fun redirect(current: BaseActivity, lessonId: Long, weekIndex: Int): RedirectBuilder {
            return RedirectBuilder
                    .redirect(current)
                    .to(LessonStatusActivity_::class.java)
                    .withExtra(EXTRA_LESSON_ID, lessonId)
                    .withExtra(EXTRA_WEEK_INDEX, weekIndex)
        }
    }

    @Bean
    lateinit var groupsService: GroupsService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var lessonStatusService: LessonStatusService

    @Bean
    lateinit var lessonsStatusAsyncService: LessonStatusAsyncService

    @Extra(EXTRA_LESSON_ID)
    @JvmField
    var lessonId: Long = 0

    @Extra(EXTRA_WEEK_INDEX)
    @JvmField
    var weekIndex: Int = 0

    @AfterViews
    fun init() {
        lessonStatusHeaderView.setLeftButtonAction { doFinish() }

        val lesson = lessonsService.getLesson(lessonId)?.lesson ?: throw RuntimeException()

        lessonStatusLessonTimeView.bind(lesson, weekIndex)
        lessonStatusTeacherInfoView.bind(lesson.teacherId)

        initButtons()
    }

    private fun initButtons() {
        val status = lessonStatusService.getLessonStatus(lessonId, lessonsService.getLessonStartTime(lessonId, weekIndex))

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
                    lessonId,
                    buttonStatus,
                    lessonsService.getLessonStartTime(lessonId, weekIndex),
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

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        setResult(Activity.RESULT_OK)
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
