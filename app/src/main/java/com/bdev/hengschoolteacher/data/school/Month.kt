package com.bdev.hengschoolteacher.data.school

import com.bdev.hengschoolteacher.R
import java.lang.RuntimeException

enum class Month(val index: Int, val nameId: Int) {
    JANUARY(0, R.string.january),
    FEBRUARY(1, R.string.february),
    MARCH(2, R.string.march),
    APRIL(3, R.string.april),
    MAY(4, R.string.may),
    JUNE(5, R.string.june),
    JULY(6, R.string.july),
    AUGUST(7, R.string.august),
    SEPTEMBER(8, R.string.september),
    OCTOBER(9, R.string.october),
    NOVEMBER(10, R.string.november),
    DECEMBER(11, R.string.december);

    companion object {
        fun findByIndex(index: Int): Month {
            return values().find { it.index == index } ?: throw RuntimeException()
        }
    }
}
