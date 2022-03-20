package com.bdev.hengschoolteacher.interactors.students

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask
import com.bdev.hengschoolteacher.network.api.students.StudentsApiProviderImpl
import javax.inject.Inject

interface StudentsLoadingInteractor {
    fun load(): SmartPromise<Unit, Exception>
}

class StudentsLoadingInteractorImpl @Inject constructor(
    private val studentsStorageInteractor: StudentsStorageInteractor,
    private val studentsApiProvider: StudentsApiProviderImpl
): StudentsLoadingInteractor {

    override fun load(): SmartPromise<Unit, Exception> {
        return SmartTask.smartTask {
            studentsStorageInteractor.setAll(
                studentsApiProvider.provide().getAllStudents().execute().body()!!
            )
        }
    }
}