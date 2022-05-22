package com.bdev.hengschoolteacher.data.school.group

import com.google.gson.annotations.SerializedName
import java.util.*

class GroupAndLesson constructor(
    @SerializedName("group") val group: Group,
    @SerializedName("lesson") val lesson: Lesson
) {
    companion object {
        fun getComparator(): Comparator<GroupAndLesson> {
            return Comparator { i1, i2 ->
                val daysComparision = i1.lesson.day.index.compareTo(i2.lesson.day.index)
                val startTimeComparision = i1.lesson.startTime.order.compareTo(i2.lesson.startTime.order)
                val finishTimeComparision = i1.lesson.finishTime.order.compareTo(i2.lesson.finishTime.order)

                return@Comparator when {
                    daysComparision != 0 -> daysComparision
                    startTimeComparision != 0 -> startTimeComparision
                    finishTimeComparision != 0 -> finishTimeComparision
                    else -> 0
                }
            }
        }
    }
}
