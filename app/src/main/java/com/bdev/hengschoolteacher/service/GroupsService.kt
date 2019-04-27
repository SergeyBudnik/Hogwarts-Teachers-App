package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.GroupsDao
import com.bdev.hengschoolteacher.dao.GroupsModel
import com.bdev.hengschoolteacher.data.school.group.Group
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class GroupsService {
    @Bean
    lateinit var groupsDao: GroupsDao

    fun getGroups(): List<Group> {
        return groupsDao.readValue().groups
    }

    fun getGroup(groupId: Long): Group? {
        return groupsDao.readValue().groups.find { it.id == groupId }
    }

    fun setGroups(groups: List<Group>) {
        groupsDao.writeValue(
                GroupsModel(groups)
        )
    }
}
