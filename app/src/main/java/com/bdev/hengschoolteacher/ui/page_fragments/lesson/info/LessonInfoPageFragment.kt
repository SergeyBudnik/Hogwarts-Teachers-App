package com.bdev.hengschoolteacher.ui.page_fragments.lesson.info

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
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
import com.bdev.hengschoolteacher.ui.page_fragments.lesson.info.adapters.LessonInfoStudentsListAdapter
import com.bdev.hengschoolteacher.ui.views.branded.BrandedButtonView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_lesson.*
import javax.inject.Inject

@AndroidEntryPoint
class LessonInfoPageFragment : BasePageFragment<LessonInfoPageFragmentViewModel>() {
    private val args: LessonInfoPageFragmentArgs by navArgs()

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

        // lessonHeaderView.setLeftButtonAction { doFinish() }

        doInit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        when (requestCode) {
//            LessonInfoActivityParams.REQUEST_CODE_LESSON_ATTENDANCE -> {
//                LessonAttendanceActivityHandler.handle(resultCode = resultCode) {
//                    handleUpdate()
//                }
//            }
//            LessonInfoActivityParams.REQUEST_CODE_LESSON_STATUS -> {
//                LessonStatusActivityHandler.handle(resultCode = resultCode) {
//                    handleUpdate()
//                }
//            }
//        }
    }

//    private fun handleUpdate() {
//        val group = groupsStorageInteractor.getById(pageFragmentData.groupId) ?: throw RuntimeException()
//        val lesson = group.lessons.find { it.id == pageFragmentData.lessonId } ?: throw RuntimeException()
//
//        if (lessonStateService.isLessonFilled(lesson, pageFragmentData.weekIndex)) {
//            // doFinish()
//        } else {
//            doInit()
//        }
//    }

    private fun `doInit`() {
        val group = groupsStorageInteractor.getById(args.args.groupId) ?: throw RuntimeException()
        val lesson = group.lessons.find { it.id == args.args.lessonId } ?: throw RuntimeException()
        val students = lessonsService.getLessonStudents(args.args.lessonId, args.args.weekIndex)
        val lessonStatus = lessonsStatusService.getLessonStatus(
            args.args.lessonId,
            lessonsService.getLessonStartTime(
                args.args.lessonId,
                args.args.weekIndex
            )
        )

        staffMembersStorageInteractor.getStaffMember(lesson.teacherLogin)?.let { teacher ->
            lessonTimeView.bind(
                    lesson = lesson,
                    lessonStartTime = lessonsService.getLessonStartTime(lesson.id, args.args.weekIndex),
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
//            LessonInfoActivityNavigation.goToStatus(
//                from = this,
//                lessonId = pageFragmentData.lessonId,
//                weekIndex = pageFragmentData.weekIndex
//            )
        }

        lessonAddTransferView.setOnClickListener {
            // TODO
        }
    }

    private fun fillLessons(lesson: Lesson, students: List<Student>) {
        val adapter = LessonInfoStudentsListAdapter(
                context = requireContext(),
                lesson = lesson,
                weekIndex = args.args.weekIndex,
                goToStudentInformationAction = { student ->
//                    LessonInfoActivityNavigation.goToStudentInformation(
//                            from = this,
//                            studentLogin = student.login
//                    )
                },
                goToStudentPaymentAction = { student ->
//                    LessonInfoActivityNavigation.goToStudentPayment(
//                            from = this,
//                            studentLogin = student.login
//                    )
                },
                goToLessonAttendanceAction = { data ->
//                    LessonInfoActivityNavigation.goToAttendance(from = this, data = data)
                }
        )

        adapter.setItems(students.map {
            Pair(
                    Pair(
                            it,
                            studentsAttendanceProviderInteractor.getAttendance(lesson.id, it.login, args.args.weekIndex)
                    ),
                    Pair(
                            studentsDebtsInteractor.getDebt(it.login),
                            studentsDebtsInteractor.getExpectedDebt(it.login)
                    )
            )
        })

        lessonStudentsListView.adapter = adapter
    }
}
