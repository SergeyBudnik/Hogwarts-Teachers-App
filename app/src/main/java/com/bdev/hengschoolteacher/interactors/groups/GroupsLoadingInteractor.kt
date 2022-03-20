package com.bdev.hengschoolteacher.interactors.groups

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.network.api.groups.GroupsApiProvider
import javax.inject.Inject

interface GroupsLoadingInteractor {
    fun load(): SmartPromise<Unit, Exception>
}

class GroupsLoadingInteractorImpl @Inject constructor(
    private val groupsApiProvider: GroupsApiProvider,
    private val groupsStorageInteractor: GroupsStorageInteractor
): GroupsLoadingInteractor {

    override fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            groupsStorageInteractor.setAll(
                    groups = groupsApiProvider.provide().getAllGroups().execute().body()!!
            )
        }
    }
}