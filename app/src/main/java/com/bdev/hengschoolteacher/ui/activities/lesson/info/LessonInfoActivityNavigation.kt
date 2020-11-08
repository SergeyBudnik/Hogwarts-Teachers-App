package com.bdev.hengschoolteacher.ui.activities.lesson.info

import android.app.Activity
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.lesson.attendance.LessonAttendanceActivityData
import com.bdev.hengschoolteacher.ui.activities.lesson.attendance.LessonAttendanceActivityLauncher
import com.bdev.hengschoolteacher.ui.activities.lesson.status.LessonStatusActivityData
import com.bdev.hengschoolteacher.ui.activities.lesson.status.LessonStatusActivityLauncher
import com.bdev.hengschoolteacher.ui.activities.student.StudentInformationActivity
import com.bdev.hengschoolteacher.ui.activities.student.StudentPaymentActivity

internal object LessonInfoActivityNavigation {
    fun goBackWithSuccess(from: LessonInfoActivity) {
        from.setResult(Activity.RESULT_OK)
        from.finish()
        from.overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    fun goBackWithCancel(from: LessonInfoActivity) {
        from.setResult(Activity.RESULT_CANCELED)
        from.finish()
        from.overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    fun goToStudentInformation(from: LessonInfoActivity, studentLogin: String) {
        StudentInformationActivity.redirectToChild(
                current = from,
                studentLogin = studentLogin
        )
    }

    fun goToStudentPayment(from: LessonInfoActivity, studentLogin: String) {
        StudentPaymentActivity.redirectToChild(
                current = from,
                studentLogin = studentLogin
        )
    }

    fun goToStatus(from: LessonInfoActivity, lessonId: Long, weekIndex: Int) {
        LessonStatusActivityLauncher.launchAsChild(
                from = from,
                data = LessonStatusActivityData(
                        lessonId = lessonId,
                        weekIndex = weekIndex
                ),
                requestCode = LessonInfoActivityParams.REQUEST_CODE_LESSON_STATUS
        )
    }

    fun goToAttendance(from: LessonInfoActivity, data: LessonAttendanceActivityData) {
        LessonAttendanceActivityLauncher.launchAsChild(
                requestCode = LessonInfoActivityParams.REQUEST_CODE_LESSON_ATTENDANCE,
                from = from,
                data = data
        )
    }
}