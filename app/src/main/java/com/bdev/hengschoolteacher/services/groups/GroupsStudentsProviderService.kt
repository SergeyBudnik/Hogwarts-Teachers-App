package com.bdev.hengschoolteacher.services.groups

import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface GroupStudentsProviderService {
    fun getAll(groupId: Long, time: Long): List<Student>
}

@EBean(scope = EBean.Scope.Singleton)
open class GroupStudentsProviderServiceImpl : GroupStudentsProviderService {
    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService

    override fun getAll(groupId: Long, time: Long): List<Student> {
        return studentsStorageService
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