package com.bdev.hengschoolteacher.services.lessons_pricing

import com.bdev.hengschoolteacher.data.school.group.GroupType
import org.androidannotations.annotations.EBean

interface LessonsPricingService {
    fun getPrice(groupType: GroupType, lengthIn30m: Int, amountOfStudents: Int, ignoreSingleStudentPrice: Boolean): Int
}

@EBean(scope = EBean.Scope.Singleton)
open class LessonsPricingServiceImpl : LessonsPricingService {
    override fun getPrice(groupType: GroupType, lengthIn30m: Int, amountOfStudents: Int, ignoreSingleStudentPrice: Boolean): Int {
        return when (groupType) {
            GroupType.GROUP -> {
                if (amountOfStudents == 1 && !ignoreSingleStudentPrice) {
                    getPriceForGroupWithSingleStudent(lengthIn30m = lengthIn30m)
                } else {
                    getPriceForGroupWithMultipleStudents(lengthIn30m = lengthIn30m)
                }
            }
            GroupType.INDIVIDUAL -> {
                getPriceForIndividual(lengthIn30m = lengthIn30m)
            }
        }
    }

    private fun getPriceForGroupWithSingleStudent(lengthIn30m: Int): Int {
        return when (lengthIn30m) {
            1 -> 350
            2 -> 700
            3 -> 1050
            4 -> 1400
            else -> 700 /* ToDo: log */
        }
    }

    private fun getPriceForGroupWithMultipleStudents(lengthIn30m: Int): Int {
        return when (lengthIn30m) {
            1 -> 240
            2 -> 480
            3 -> 700
            4 -> 1000
            else -> 700 /* ToDo: log */
        }
    }

    private fun getPriceForIndividual(lengthIn30m: Int): Int {
        return when (lengthIn30m) {
            1 -> 600
            2 -> 1200
            3 -> 1800
            4 -> 2400
            else -> 1200 /* ToDO: log */
        }
    }
}