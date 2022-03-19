package com.bdev.hengschoolteacher.ui.activities.lesson.info

import android.annotation.SuppressLint
import android.content.Intent
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.interactors.LessonStateServiceImpl
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonStatusStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.groups.GroupsStorageInteractor
import com.bdev.hengschoolteacher.interactors.groups.GroupsStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractorImpl
import com.bdev.hengschoolteacher.interactors.staff.StaffMembersStorageServiceImpl
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderServiceImpl
import com.bdev.hengschoolteacher.interactors.students_debts.StudentDebtsService
import com.bdev.hengschoolteacher.interactors.students_debts.StudentDebtsServiceImpl
import com.bdev.hengschoolteacher.interactors.teacher.TeacherInfoServiceImpl
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

    @Bean(GroupsStorageInteractorImpl::class)
    lateinit var groupsStorageInteractor: GroupsStorageInteractor
    @Bean(StudentsStorageInteractorImpl::class)
    lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Bean
    lateinit var lessonsService: LessonsInteractorImpl
    @Bean
    lateinit var lessonStatusService: LessonStatusStorageInteractorImpl
    @Bean
    lateinit var lessonStateService: LessonStateServiceImpl
    @Bean(StudentDebtsServiceImpl::class)
    lateinit var studentsDebtsService: StudentDebtsService
    @Bean
    lateinit var studentsAttendanceProviderService: StudentsAttendancesProviderServiceImpl
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageServiceImpl
    @Bean
    lateinit var teacherInfoService: TeacherInfoServiceImpl

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
        val group = groupsStorageInteractor.getById(activityData.groupId) ?: throw RuntimeException()
        val lesson = group.lessons.find { it.id == activityData.lessonId } ?: throw RuntimeException()

        if (lessonStateService.isLessonFilled(lesson, activityData.weekIndex)) {
            doFinish()
        } else {
            doInit()
        }
    }

    private fun doInit() {
        val group = groupsStorageInteractor.getById(activityData.groupId) ?: throw RuntimeException()
        val lesson = group.lessons.find { it.id == activityData.lessonId } ?: throw RuntimeException()
        val students = lessonsService.getLessonStudents(activityData.lessonId, activityData.weekIndex)
        val lessonStatus = lessonStatusService.getLessonStatus(
                activityData.lessonId,
                lessonsService.getLessonStartTime(
                        activityData.lessonId,
                        activityData.weekIndex
                )
        )

        staffMembersStorageService.getStaffMember(lesson.teacherLogin)?.let { teacher ->
            lessonTimeView.bind(
                    lesson = lesson,
                    lessonStartTime = lessonsService.getLessonStartTime(lesson.id, activityData.weekIndex),
                    teacherName = teacherInfoService.getTeachersName(teacher),
                    teacherSurname = teacherInfoService.getTeachersSurname(teacher)
            )
        }

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

        adapter.setItems(students.map {
            Pair(
                    Pair(
                            it,
                            studentsAttendanceProviderService.getAttendance(lesson.id, it.login, activityData.weekIndex)
                    ),
                    Pair(
                            studentsDebtsService.getDebt(it.login),
                            studentsDebtsService.getExpectedDebt(it.login)
                    )
            )
        })

        lessonStudentsListView.adapter = adapter
    }

    override fun onBackPressed() = LessonInfoActivityNavigation.goBackWithCancel(from = this)

    private fun doFinish() = LessonInfoActivityNavigation.goBackWithSuccess(from = this)

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
