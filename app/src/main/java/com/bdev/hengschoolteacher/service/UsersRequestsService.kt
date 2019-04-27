package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.UsersRequestModel
import com.bdev.hengschoolteacher.dao.UsersRequestsDao
import com.bdev.hengschoolteacher.data.school.requests.UserRequest
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class UsersRequestsService {
    @Bean
    lateinit var usersRequestsDao: UsersRequestsDao

    fun setUsersRequests(usersRequests: List<UserRequest>) {
        usersRequestsDao.writeValue(UsersRequestModel(usersRequests))
    }

    fun getUsersRequests(): List<UserRequest> {
        return usersRequestsDao.readValue().usersRequests
    }

    fun getUserRequest(id: Long): UserRequest? {
        return usersRequestsDao.readValue().usersRequests.find { it.id == id }
    }
}
