package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.UsersRequestsDao
import com.bdev.hengschoolteacher.data.school.requests.UserRequest
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class UsersRequestsService {
    @Bean
    lateinit var usersRequestsDao: UsersRequestsDao

    fun setUsersRequests(usersRequests: List<UserRequest>) {
        usersRequestsDao.setUsersRequests(usersRequests)
    }

    fun getUsersRequests(): List<UserRequest> {
        return usersRequestsDao.getUsersRequests()
    }

    fun getUserRequest(id: Long): UserRequest? {
        return usersRequestsDao.getUsersRequests().find { it.id == id }
    }
}
