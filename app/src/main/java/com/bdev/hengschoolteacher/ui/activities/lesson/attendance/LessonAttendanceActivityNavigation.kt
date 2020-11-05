package com.bdev.hengschoolteacher.ui.activities.lesson.attendance

import android.app.Activity
import com.bdev.hengschoolteacher.R

internal object LessonAttendanceActivityNavigation {
    fun goBackWithSuccess(from: LessonAttendanceActivity) {
        from.setResult(Activity.RESULT_OK)
        from.finish()
        from.overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    fun goBackWithCancel(from: LessonAttendanceActivity) {
        from.setResult(Activity.RESULT_CANCELED)
        from.finish()
        from.overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}