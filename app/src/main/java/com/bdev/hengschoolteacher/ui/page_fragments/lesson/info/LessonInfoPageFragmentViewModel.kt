package com.bdev.hengschoolteacher.ui.page_fragments.lesson.info

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface LessonInfoPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class LessonInfoPageFragmentViewModelImpl @Inject constructor(
) : LessonInfoPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}