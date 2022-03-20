package com.bdev.hengschoolteacher.interactors.staff_members

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.network.api.staff_members.StaffMembersApiProvider
import javax.inject.Inject

interface StaffMembersLoadingInteractor {
    fun load(): SmartPromise<Unit, Exception>
}

class StaffMembersLoadingInteractorImpl @Inject constructor(
    private val staffMembersApiProvider: StaffMembersApiProvider,
    private val staffMembersStorageInteractor: StaffMembersStorageInteractor,
): StaffMembersLoadingInteractor {
    override fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            staffMembersStorageInteractor.setAllStaffMembers(
                staffMembersApiProvider.provide().getAllStaffMembers().execute().body()!!
            )
        }
    }
}
