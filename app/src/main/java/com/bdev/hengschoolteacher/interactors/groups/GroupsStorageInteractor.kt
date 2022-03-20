package com.bdev.hengschoolteacher.interactors.groups

import com.bdev.hengschoolteacher.dao.GroupsDao
import com.bdev.hengschoolteacher.dao.GroupsModel
import com.bdev.hengschoolteacher.data.school.group.Group
import javax.inject.Inject

interface GroupsStorageInteractor {
    fun getAll(): List<Group>
    fun getById(groupId: Long): Group?
    fun setAll(groups: List<Group>)
}

class GroupsStorageInteractorImpl @Inject constructor(
    private val groupsDao: GroupsDao
): GroupsStorageInteractor {
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
