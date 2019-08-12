package com.bdev.hengschoolteacher.ui.activities.lesson

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.LessonsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import kotlinx.android.synthetic.main.activity_lesson_transfer.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra

@SuppressLint("Registered")
@EActivity(R.layout.activity_lesson_transfer)
open class LessonTransferActivity : BaseActivity() {
    companion object {
        private const val EXTRA_GROUP_ID = "EXTRA_GROUP_ID"
        private const val EXTRA_LESSON_ID = "EXTRA_LESSON_ID"
        private const val EXTRA_WEEK_INDEX = "EXTRA_WEEK_INDEX"

        fun redirectToChild(
                context: Context,
                groupId: Long,
                lessonId: Long,
                weekIndex: Int,
                requestCode: Int
        ) {
            RedirectBuilder
                    .redirect(context as BaseActivity)
                    .to(LessonTransferActivity_::class.java)
                    .withExtra(EXTRA_GROUP_ID, groupId)
                    .withExtra(EXTRA_LESSON_ID, lessonId)
                    .withExtra(EXTRA_WEEK_INDEX, weekIndex)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .goForResult(requestCode)
        }
    }

    @Extra(EXTRA_GROUP_ID)
    @JvmField
    var groupId: Long = 0L

    @Extra(EXTRA_LESSON_ID)
    @JvmField
    var lessonId: Long = 0L

    @Extra(EXTRA_WEEK_INDEX)
    @JvmField
    var weekIndex: Int = 0

    @Bean
    lateinit var lessonsService: LessonsService

    @AfterViews
    fun init() {
        lessonTransferHeaderView.setLeftButtonAction { doFinish() }

        doInit()
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doInit() {
        val lesson = lessonsService.getLesson(lessonId)?.lesson

        if (lesson != null) {
            lessonTransferTimeView.bind(
                    lesson = lesson,
                    weekIndex = weekIndex
            )

            lessonTransferTeacherInfoView.bind(
                    teacherId = lesson.teacherId
            )
        }

    }

    private fun doFinish() {
        setResult(Activity.RESULT_OK)
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
