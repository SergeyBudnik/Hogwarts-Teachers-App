package com.bdev.hengschoolteacher.interactors.groups

import com.bdev.hengschoolteacher.async.CommonAsyncService
import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.rest.GroupsRest
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

interface GroupsLoadingInteractor {
    fun load(): SmartPromise<Unit, Exception>
}

@EBean(scope = EBean.Scope.Singleton)
open class GroupsLoadingInteractorImpl : CommonAsyncService(), GroupsLoadingInteractor {
    @RestService
    lateinit var groupsRest: GroupsRest

    @Bean
    lateinit var authService: AuthStorageInteractorImpl

    @Bean(GroupsStorageInteractorImpl::class)
    lateinit var groupsStorageInteractor: GroupsStorageInteractor

    override fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(listOf(groupsRest), authService.getAuthInfo())

            groupsStorageInteractor.setAll(
                    groups = groupsRest.getAllGroups()
            )
        }
    }
}