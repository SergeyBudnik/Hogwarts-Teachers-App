package com.bdev.hengschoolteacher.ui.page_fragments.lesson.info

import com.bdev.hengschoolteacher.ui.page_fragments.lesson.attendance.LessonAttendanceActivityData

internal object LessonInfoActivityNavigation {
    fun goBackWithSuccess(from: LessonInfoPageFragment) {
//        from.setResult(Activity.RESULT_OK)
//        from.finish()
//        from.overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    fun goBackWithCancel(from: LessonInfoPageFragment) {
//        from.setResult(Activity.RESULT_CANCELED)
//        from.finish()
//        from.overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    fun goToStudentInformation(from: LessonInfoPageFragment, studentLogin: String) {
//        StudentInformationPageFragment.redirectToChild(
//                current = from,
//                studentLogin = studentLogin
//        )
    }

    fun goToStudentPayment(from: LessonInfoPageFragment, studentLogin: String) {
//        StudentPaymentPageFragment.redirectToChild(
//                current = from,
//                studentLogin = studentLogin
//        )
    }

    fun goToStatus(from: LessonInfoPageFragment, lessonId: Long, weekIndex: Int) {
//        LessonStatusActivityLauncher.launchAsChild(
//                from = from,
//                data = LessonStatusActivityData(
//                        lessonId = lessonId,
//                        weekIndex = weekIndex
//                ),
//                requestCode = LessonInfoActivityParams.REQUEST_CODE_LESSON_STATUS
//        )
    }

    fun goToAttendance(from: LessonInfoPageFragment, data: LessonAttendanceActivityData) {
//        LessonAttendanceActivityLauncher.launchAsChild(
//                requestCode = LessonInfoActivityParams.REQUEST_CODE_LESSON_ATTENDANCE,
//                from = from,
//                data = data
//        )
    }
}