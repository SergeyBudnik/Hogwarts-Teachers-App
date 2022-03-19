package com.bdev.hengschoolteacher.interactors.teacher

import com.bdev.hengschoolteacher.data.school.teacher.TeacherActionType
import com.bdev.hengschoolteacher.data.school.teacher.TeacherPayment
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonStatusStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractorImpl
import com.bdev.hengschoolteacher.interactors.staff.StaffMembersStorageServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface TeacherSalaryService {
    fun getTeacherPayments(teacherLogin: String, weekIndex: Int): List<TeacherPayment>
}

@EBean
open class TeacherSalaryServiceImpl : TeacherSalaryService {
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageServiceImpl
    @Bean
    lateinit var lessonsService: LessonsInteractorImpl
    @Bean
    lateinit var lessonStatusService: LessonStatusStorageInteractorImpl

    @Bean
    lateinit var teacherActionsService: TeacherActionsServiceImpl

    override fun getTeacherPayments(teacherLogin: String, weekIndex: Int): List<TeacherPayment> {
        val teacherPayments = ArrayList<TeacherPayment>()

        val staffMember = staffMembersStorageService.getStaffMember(login = teacherLogin)

        return if (staffMember != null) {
            val teacherActions = teacherActionsService.getTeacherActions(teacherLogin, weekIndex)

            for (teacherAction in teacherActions) {
                when (teacherAction.type) {
                    TeacherActionType.ROAD -> teacherPayments.add(TeacherPayment(
                            staffMember.salaryIn30m,
                            teacherAction
                    ))
                    TeacherActionType.LESSON,
                    TeacherActionType.ONLINE_LESSON -> teacherPayments.add(TeacherPayment(
                            3 * staffMember.salaryIn30m * (teacherAction.finishTime.order - teacherAction.startTime.order) / 2,
                            teacherAction
                    ))
                }
            }

            teacherPayments
        } else {
            emptyList()
        }
    }
}
