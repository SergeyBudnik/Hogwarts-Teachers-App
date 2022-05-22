package com.bdev.hengschoolteacher.ui.page_fragments.lesson.attendance

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface LessonAttendancePageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class LessonAttendancePageFragmentViewModelImpl @Inject constructor(
): LessonAttendancePageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}