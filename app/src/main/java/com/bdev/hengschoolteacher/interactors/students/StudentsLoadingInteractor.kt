package com.bdev.hengschoolteacher.interactors.students

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import com.bdev.hengschoolteacher.network.api.students.StudentsApiProviderImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentsLoadingInteractor {
    fun load(): SmartPromise<Unit, Exception>
}

@EBean
open class StudentsLoadingInteractorImpl : StudentsLoadingInteractor {
    @Bean
    lateinit var authService: AuthStorageInteractorImpl

    @Bean(StudentsStorageInteractorImpl::class)
    lateinit var studentsStorageInteractor: StudentsStorageInteractor

    @Bean
    lateinit var studentsApiProvider: StudentsApiProviderImpl

    override fun load(): SmartPromise<Unit, Exception> {
        return SmartTask.smartTask {
            studentsStorageInteractor.setAll(
                studentsApiProvider.provide().getAllStudents().execute().body()!!
            )
        }
    }
}