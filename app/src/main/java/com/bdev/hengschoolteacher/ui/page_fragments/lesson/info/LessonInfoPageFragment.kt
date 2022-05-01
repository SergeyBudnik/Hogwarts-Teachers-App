package com.bdev.hengschoolteacher.ui.page_fragments.lesson.info

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.interactors.LessonStateService
import com.bdev.hengschoolteacher.interactors.groups.GroupsStorageInteractor
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusStorageInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderInteractor
import com.bdev.hengschoolteacher.interactors.students_debts.StudentsDebtsInteractor
import com.bdev.hengschoolteacher.interactors.teachers.TeacherInfoInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.lesson.attendance.LessonAttendanceActivityHandler
import com.bdev.hengschoolteacher.ui.page_fragments.lesson.info.adapters.LessonInfoStudentsListAdapter
import com.bdev.hengschoolteacher.ui.page_fragments.lesson.status.LessonStatusActivityHandler
import com.bdev.hengschoolteacher.ui.views.app.root.HtPageRootView
import com.bdev.hengschoolteacher.ui.views.branded.BrandedButtonView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_lesson.*
import javax.inject.Inject

@AndroidEntryPoint
class LessonInfoPageFragment : BasePageFragment<LessonInfoPageFragmentViewModel>() {
    lateinit var activityData: LessonInfoActivityData

    @Inject lateinit var groupsStorageInteractor: GroupsStorageInteractor
    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var lessonsService: LessonsInteractor
    @Inject lateinit var lessonsStatusService: LessonsStatusStorageInteractor
    @Inject lateinit var lessonStateService: LessonStateService
    @Inject lateinit var studentsDebtsInteractor: StudentsDebtsInteractor
    @Inject lateinit var studentsAttendanceProviderInteractor: StudentsAttendancesProviderInteractor
    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor
    @Inject lateinit var teacherInfoInteractor: TeacherInfoInteractor

    override fun provideViewModel(): LessonInfoPageFragmentViewModel =
        ViewModelProvider(this).get(LessonInfoPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_lesson, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        // activityData = intent.getSerializableExtra(EXTRA_DATA) as LessonInfoActivityData todo

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
        val lessonStatus = lessonsStatusService.getLessonStatus(
                activityData.lessonId,
                lessonsService.getLessonStartTime(
                        activityData.lessonId,
                        activityData.weekIndex
                )
        )

        staffMembersStorageInteractor.getStaffMember(lesson.teacherLogin)?.let { teacher ->
            lessonTimeView.bind(
                    lesson = lesson,
                    lessonStartTime = lessonsService.getLessonStartTime(lesson.id, activityData.weekIndex),
                    teacherName = teacherInfoInteractor.getTeachersName(teacher),
                    teacherSurname = teacherInfoInteractor.getTeachersSurname(teacher)
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
                context = requireContext(),
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
                            studentsAttendanceProviderInteractor.getAttendance(lesson.id, it.login, activityData.weekIndex)
                    ),
                    Pair(
                            studentsDebtsInteractor.getDebt(it.login),
                            studentsDebtsInteractor.getExpectedDebt(it.login)
                    )
            )
        })

        lessonStudentsListView.adapter = adapter
    }

    private fun doFinish() = LessonInfoActivityNavigation.goBackWithSuccess(from = this)
}
