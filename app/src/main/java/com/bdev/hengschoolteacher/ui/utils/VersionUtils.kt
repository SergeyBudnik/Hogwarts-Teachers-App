package com.bdev.hengschoolteacher.ui.utils

import com.bdev.hengschoolteacher.BuildConfig

class VersionUtils {
    fun getVersion(): String {
        return BuildConfig.VERSION_NAME
    }
}
