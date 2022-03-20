package com.bdev.hengschoolteacher.ui.activities.lesson.info

import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder

object LessonInfoActivityLauncher {
    fun launchAsChild(from: BaseActivity, requestCode: Int, data: LessonInfoActivityData) {
        RedirectBuilder
                .redirect(from)
                .to(LessonInfoActivity::class.java)
                .withExtra(LessonInfoActivityParams.EXTRA_DATA, data)
                .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                .goForResult(requestCode)
    }
}