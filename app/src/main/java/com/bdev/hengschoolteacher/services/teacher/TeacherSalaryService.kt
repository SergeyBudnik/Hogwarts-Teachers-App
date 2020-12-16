package com.bdev.hengschoolteacher.services.teacher

import com.bdev.hengschoolteacher.data.school.teacher.TeacherActionType
import com.bdev.hengschoolteacher.data.school.teacher.TeacherPayment
import com.bdev.hengschoolteacher.services.LessonStatusService
import com.bdev.hengschoolteacher.services.lessons.LessonsService
import com.bdev.hengschoolteacher.services.staff.StaffMembersStorageService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class TeacherSalaryService {
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var lessonStatusService: LessonStatusService

    @Bean
    lateinit var teacherActionsService: TeacherActionsService

    fun getTeacherPayments(teacherLogin: String, weekIndex: Int): List<TeacherPayment> {
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
