package com.bdev.hengschoolteacher.ui.page_fragments.lesson.status

import android.app.Activity

object LessonStatusActivityHandler {
    fun handle(resultCode: Int, action: () -> Unit) {
        if (resultCode == Activity.RESULT_OK) {
            action()
        }
    }
}