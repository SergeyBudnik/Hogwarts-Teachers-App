package com.bdev.hengschoolteacher.ui.navigation

import androidx.navigation.NavDirections

data class NavCommand(
    val navDir: NavDirections?,
    val closeCurrent: Boolean,
    val closeAll: Boolean,
    val quitApp: Boolean
) {
    companion object {
        fun forward(navDir: NavDirections) = NavCommand(
            navDir = navDir,
            closeCurrent = false,
            closeAll = false,
            quitApp = false
        )

        fun replace(navDir: NavDirections) = NavCommand(
            navDir = navDir,
            closeCurrent = true,
            closeAll = false,
            quitApp = false
        )

        fun top(navDir: NavDirections) = NavCommand(
            navDir = navDir,
            closeCurrent = false,
            closeAll = true,
            quitApp = false
        )

        fun quit() = NavCommand(
            navDir = null,
            closeCurrent = false,
            closeAll = false,
            quitApp = true
        )
    }
}