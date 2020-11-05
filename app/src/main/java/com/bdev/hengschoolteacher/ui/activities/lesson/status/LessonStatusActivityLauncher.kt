package com.bdev.hengschoolteacher.ui.activities.lesson.status

import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder

object LessonStatusActivityLauncher {
    fun launchAsChild(from: BaseActivity, requestCode: Int, data: LessonStatusActivityData) {
        RedirectBuilder
                .redirect(from)
                .to(LessonStatusActivity_::class.java)
                .withExtra(LessonStatusActivity.EXTRA_DATA, data)
                .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                .goForResult(requestCode)
    }
}