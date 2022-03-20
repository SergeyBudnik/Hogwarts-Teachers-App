package com.bdev.hengschoolteacher.interactors.lessons_pricing

import com.bdev.hengschoolteacher.data.school.group.GroupType
import javax.inject.Inject

interface LessonsPricingInteractor {
    fun getPrice(groupType: GroupType, lengthIn30m: Int, amountOfStudents: Int, ignoreSingleStudentPrice: Boolean): Int
}

class LessonsPricingInteractorImpl @Inject constructor(): LessonsPricingInteractor {
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
            1 -> 375
            2 -> 750
            3 -> 1125
            4 -> 1500
            else -> 750 /* ToDo: log */
        }
    }

    private fun getPriceForGroupWithMultipleStudents(lengthIn30m: Int): Int {
        return when (lengthIn30m) {
            1 -> 250
            2 -> 500
            3 -> 750
            4 -> 1000
            else -> 750 /* ToDo: log */
        }
    }

    private fun getPriceForIndividual(lengthIn30m: Int): Int {
        return when (lengthIn30m) {
            1 ->  750
            2 -> 1500
            3 -> 2250
            4 -> 3000
            else -> 1500 /* ToDO: log */
        }
    }
}