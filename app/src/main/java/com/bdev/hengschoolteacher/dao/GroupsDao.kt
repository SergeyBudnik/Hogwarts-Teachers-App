package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.group.Group
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import java.io.Serializable

class GroupsModel : Serializable {
    var groups: List<Group> = emptyList()
}

@EBean(scope = EBean.Scope.Singleton)
open class GroupsDao : CommonDao<GroupsModel>() {
    @RootContext
    lateinit var rootContext: Context

    fun setGroups(groups: List<Group>) {
        readCache()

        getValue().groups = groups

        persist()
    }

    fun getGroups(): List<Group> {
        readCache()

        return getValue().groups
    }

    override fun getContext(): Context { return rootContext }
    override fun getFileName(): String { return GroupsDao::class.java.canonicalName }
    override fun newInstance(): GroupsModel { return GroupsModel() }
}
