package com.bdev.hengschoolteacher.ui.activities.lesson.status

import android.app.Activity
import com.bdev.hengschoolteacher.R

object LessonStatusActivityNavigation {
    fun goBackWithSuccess(from: LessonStatusActivity) {
        from.setResult(Activity.RESULT_OK)
        from.finish()
        from.overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    fun goBackWithCancel(from: LessonStatusActivity) {
        from.setResult(Activity.RESULT_CANCELED)
        from.finish()
        from.overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}