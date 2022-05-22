package com.bdev.hengschoolteacher.ui.page_fragments.lesson.attendance

import android.app.Activity

object LessonAttendanceActivityHandler {
    fun handle(resultCode: Int, action: () -> Unit) {
        if (resultCode == Activity.RESULT_OK) {
            action()
        }
    }
}