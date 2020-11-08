package com.bdev.hengschoolteacher.ui.activities.lesson.info

import android.annotation.SuppressLint
import android.content.Intent
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.services.*
import com.bdev.hengschoolteacher.services.groups.GroupsStorageService
import com.bdev.hengschoolteacher.services.groups.GroupsStorageServiceImpl
import com.bdev.hengschoolteacher.services.lessons.LessonsService
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.lesson.attendance.LessonAttendanceActivityHandler
import com.bdev.hengschoolteacher.ui.activities.lesson.info.LessonInfoActivityParams.EXTRA_DATA
import com.bdev.hengschoolteacher.ui.activities.lesson.info.adapters.LessonInfoStudentsListAdapter
import com.bdev.hengschoolteacher.ui.activities.lesson.status.LessonStatusActivityHandler
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.branded.BrandedButtonView
import kotlinx.android.synthetic.main.activity_lesson.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra

@SuppressLint("Registered")
@EActivity(R.layout.activity_lesson)
open class LessonInfoActivity : BaseActivity() {
    @Extra(EXTRA_DATA)
    lateinit var activityData: LessonInfoActivityData

    @Bean(GroupsStorageServiceImpl::class)
    lateinit var groupsStorageService: GroupsStorageService
    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var lessonStatusService: LessonStatusService
    @Bean
    lateinit var lessonStateService: LessonStateService

    @AfterViews
    fun init() {
        lessonHeaderView.setLeftButtonAction { doFinish() }

        doInit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            LessonInfoActivityParams.REQUEST_CODE_LESSON_ATTENDANCE -> {
                LessonAttendanceActivityHandler.handle(resultCode = resultCode) {
                    handleUpdate()
                }
            }
            LessonInfoActivityParams.REQUEST_CODE_LESSON_STATUS -> {
                LessonStatusActivityHandler.handle(resultCode = resultCode) {
                    handleUpdate()
                }
            }
        }
    }

    private fun handleUpdate() {
        val group = groupsStorageService.getById(activityData.groupId) ?: throw RuntimeException()
        val lesson = group.lessons.find { it.id == activityData.lessonId } ?: throw RuntimeException()

        if (lessonStateService.isLessonFilled(lesson, activityData.weekIndex)) {
            doFinish()
        } else {
            doInit()
        }
    }

    private fun doInit() {
        val group = groupsStorageService.getById(activityData.groupId) ?: throw RuntimeException()
        val lesson = group.lessons.find { it.id == activityData.lessonId } ?: throw RuntimeException()
        val students = lessonsService.getLessonStudents(activityData.lessonId, activityData.weekIndex)
        val lessonStatus = lessonStatusService.getLessonStatus(
                activityData.lessonId,
                lessonsService.getLessonStartTime(
                        activityData.lessonId,
                        activityData.weekIndex
                )
        )

        lessonTimeView.bind(lesson, activityData.weekIndex)

        fillLessons(lesson, students)

        if (lessonStatus != null) {
            lessonMarkStatusView.setText(when (lessonStatus.type) {
                LessonStatus.Type.FINISHED -> "Занятие проведено"
                LessonStatus.Type.CANCELED -> "Занятие отменено"
                LessonStatus.Type.MOVED -> "Занятие перенесено"
            })
            lessonMarkStatusView.setStyle(BrandedButtonView.Style.DEFAULT)
        }

        lessonMarkStatusView.setOnClickListener {
            LessonInfoActivityNavigation.goToStatus(
                    from = this,
                    lessonId = activityData.lessonId,
                    weekIndex = activityData.weekIndex
            )
        }

        lessonAddTransferView.setOnClickListener {
            // TODO
        }
    }

    private fun fillLessons(lesson: Lesson, students: List<Student>) {
        val adapter = LessonInfoStudentsListAdapter(
                context = this,
                lesson = lesson,
                weekIndex = activityData.weekIndex,
                goToStudentInformationAction = { student ->
                    LessonInfoActivityNavigation.goToStudentInformation(
                            from = this,
                            studentLogin = student.login
                    )
                },
                goToStudentPaymentAction = { student ->
                    LessonInfoActivityNavigation.goToStudentPayment(
                            from = this,
                            studentLogin = student.login
                    )
                },
                goToLessonAttendanceAction = { data ->
                    LessonInfoActivityNavigation.goToAttendance(from = this, data = data)
                }
        )

        adapter.setItems(students)

        lessonStudentsListView.adapter = adapter
    }

    override fun onBackPressed() = LessonInfoActivityNavigation.goBackWithCancel(from = this)

    private fun doFinish() = LessonInfoActivityNavigation.goBackWithSuccess(from = this)

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
