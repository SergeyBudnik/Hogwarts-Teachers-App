package com.bdev.hengschoolteacher.interactors.groups

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import com.bdev.hengschoolteacher.network.api.groups.GroupsApiProviderImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface GroupsLoadingInteractor {
    fun load(): SmartPromise<Unit, Exception>
}

@EBean(scope = EBean.Scope.Singleton)
open class GroupsLoadingInteractorImpl : GroupsLoadingInteractor {
    @Bean
    lateinit var groupsApiProvider: GroupsApiProviderImpl

    @Bean
    lateinit var authService: AuthStorageInteractorImpl

    @Bean(GroupsStorageInteractorImpl::class)
    lateinit var groupsStorageInteractor: GroupsStorageInteractor

    override fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            groupsStorageInteractor.setAll(
                    groups = groupsApiProvider.provide().getAllGroups().execute().body()!!
            )
        }
    }
}