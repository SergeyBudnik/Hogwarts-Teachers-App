package com.bdev.hengschoolteacher.interactors.teachers

import com.bdev.hengschoolteacher.data.school.teacher.TeacherActionType
import com.bdev.hengschoolteacher.data.school.teacher.TeacherPayment
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import javax.inject.Inject

interface TeacherSalaryInteractor {
    fun getTeacherPayments(teacherLogin: String, weekIndex: Int): List<TeacherPayment>
}

class TeacherSalaryInteractorImpl @Inject constructor(
    private val staffMembersStorageInteractor: StaffMembersStorageInteractor,
    private val teacherActionsInteractor: TeacherActionsInteractor
): TeacherSalaryInteractor {
    override fun getTeacherPayments(teacherLogin: String, weekIndex: Int): List<TeacherPayment> {
        val teacherPayments = ArrayList<TeacherPayment>()

        val staffMember = staffMembersStorageInteractor.getStaffMember(login = teacherLogin)

        return if (staffMember != null) {
            val teacherActions = teacherActionsInteractor.getTeacherActions(teacherLogin, weekIndex)

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
