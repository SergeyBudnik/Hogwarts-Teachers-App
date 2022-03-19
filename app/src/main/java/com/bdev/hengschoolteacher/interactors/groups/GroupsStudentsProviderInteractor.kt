package com.bdev.hengschoolteacher.interactors.groups

import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractorImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface GroupStudentsProviderService {
    fun getAll(groupId: Long, time: Long): List<Student>
}

@EBean(scope = EBean.Scope.Singleton)
open class GroupStudentsProviderServiceImpl : GroupStudentsProviderService {
    @Bean(StudentsStorageInteractorImpl::class)
    lateinit var studentsStorageInteractor: StudentsStorageInteractor

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