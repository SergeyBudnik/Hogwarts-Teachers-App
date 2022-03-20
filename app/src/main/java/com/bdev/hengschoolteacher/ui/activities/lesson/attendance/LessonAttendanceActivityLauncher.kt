package com.bdev.hengschoolteacher.ui.activities.lesson.attendance

import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder

object LessonAttendanceActivityLauncher {
    fun launchAsChild(from: BaseActivity, requestCode: Int, data: LessonAttendanceActivityData) {
        RedirectBuilder
                .redirect(from)
                .to(LessonAttendanceActivity::class.java)
                .withExtra(LessonAttendanceActivityParams.EXTRA_DATA, data)
                .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                .goForResult(requestCode)
    }
}