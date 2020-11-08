package com.bdev.hengschoolteacher.services.groups

import com.bdev.hengschoolteacher.dao.GroupsDao
import com.bdev.hengschoolteacher.dao.GroupsModel
import com.bdev.hengschoolteacher.data.school.group.Group
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface GroupsStorageService {
    fun getAll(): List<Group>
    fun getById(groupId: Long): Group?
    fun setAll(groups: List<Group>)
}

@EBean(scope = EBean.Scope.Singleton)
open class GroupsStorageServiceImpl : GroupsStorageService {
    @Bean
    lateinit var groupsDao: GroupsDao

    override fun getAll(): List<Group> {
        return groupsDao.readValue().groups
    }

    override fun getById(groupId: Long): Group? {
        return groupsDao.readValue().groups.find { it.id == groupId }
    }

    override fun setAll(groups: List<Group>) {
        groupsDao.writeValue(
                GroupsModel(groups)
        )
    }
}
