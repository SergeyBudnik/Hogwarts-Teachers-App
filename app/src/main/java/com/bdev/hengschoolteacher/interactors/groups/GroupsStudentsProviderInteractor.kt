package com.bdev.hengschoolteacher.interactors.groups

import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import javax.inject.Inject

interface GroupsStudentsProviderInteractor {
    fun getAll(groupId: Long, time: Long): List<Student>
}

class GroupsStudentsProviderInteractorImpl @Inject constructor(
    private val studentsStorageInteractor: StudentsStorageInteractor
): GroupsStudentsProviderInteractor {
    override fun getAll(groupId: Long, time: Long): List<Student> {
        return studentsStorageInteractor
                .getAll()
                .filter { student ->
                    student.studentGroups
                            .filter { it.startTime <= time }
                            .filter { it.finishTime >= time }
                            .map { it.groupId }
                            .contains(groupId)
                }
    }
}