package com.bdev.hengschoolteacher.services.groups

import com.bdev.hengschoolteacher.async.CommonAsyncService
import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.rest.GroupsRest
import com.bdev.hengschoolteacher.services.auth.AuthService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

interface GroupsLoadingService {
    fun load(): SmartPromise<Unit, Exception>
}

@EBean(scope = EBean.Scope.Singleton)
open class GroupsLoadingServiceImpl : CommonAsyncService(), GroupsLoadingService {
    @RestService
    lateinit var groupsRest: GroupsRest

    @Bean
    lateinit var authService: AuthService

    @Bean(GroupsStorageServiceImpl::class)
    lateinit var groupsStorageService: GroupsStorageService

    override fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(listOf(groupsRest), authService.getAuthInfo())

            groupsStorageService.setAll(
                    groups = groupsRest.getAllGroups()
            )
        }
    }
}