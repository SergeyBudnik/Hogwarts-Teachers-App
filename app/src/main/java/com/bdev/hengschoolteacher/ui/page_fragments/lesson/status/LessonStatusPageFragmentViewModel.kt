package com.bdev.hengschoolteacher.ui.page_fragments.lesson.status

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface LessonStatusPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class LessonStatusPageFragmentViewModelImpl @Inject constructor(
) : LessonStatusPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}