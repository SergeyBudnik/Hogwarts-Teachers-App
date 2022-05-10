package com.bdev.hengschoolteacher.ui.fragments.common.content

import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButtons

interface BaseContentFragmentData {
    fun isVisible(): Boolean
    fun getHeaderButtons(): AppHeaderButtons
}