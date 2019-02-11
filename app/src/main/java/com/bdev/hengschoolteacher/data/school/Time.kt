package com.bdev.hengschoolteacher.data.school

import com.bdev.hengschoolteacher.R
import java.lang.RuntimeException

enum class Time constructor(
    val order: Int,
    val translationId: Int
) {
    T_07_00(1,  R.string.t_07_00), T_07_30(2,  R.string.t_07_30),
    T_08_00(3,  R.string.t_08_00), T_08_30(4,  R.string.t_08_30),
    T_09_00(5,  R.string.t_09_00), T_09_30(6,  R.string.t_09_30),
    T_10_00(7,  R.string.t_10_00), T_10_30(8,  R.string.t_10_30),
    T_11_00(9,  R.string.t_11_00), T_11_30(10, R.string.t_11_30),
    T_12_00(11, R.string.t_12_00), T_12_30(12, R.string.t_12_30),
    T_13_00(13, R.string.t_13_00), T_13_30(14, R.string.t_13_30),
    T_14_00(15, R.string.t_14_00), T_14_30(16, R.string.t_14_30),
    T_15_00(17, R.string.t_15_00), T_15_30(18, R.string.t_15_30),
    T_16_00(19, R.string.t_16_00), T_16_30(20, R.string.t_16_30),
    T_17_00(21, R.string.t_17_00), T_17_30(22, R.string.t_17_30),
    T_18_00(23, R.string.t_18_00), T_18_30(24, R.string.t_18_30),
    T_19_00(25, R.string.t_19_00), T_19_30(26, R.string.t_19_30),
    T_20_00(27, R.string.t_20_00), T_20_30(28, R.string.t_20_30),
    T_21_00(29, R.string.t_21_00), T_21_30(30, R.string.t_21_30);

    companion object {
        fun fromOrder(order: Int): Time {
            return values().find { it.order == order } ?: throw RuntimeException()
        }
    }
}
