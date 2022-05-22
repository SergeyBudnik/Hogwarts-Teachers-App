package com.bdev.hengschoolteacher.ui.page_fragments.lesson.info

import androidx.lifecycle.SavedStateHandle
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface LessonInfoPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class LessonInfoPageFragmentViewModelImpl @Inject constructor(
    savedStateHandle: SavedStateHandle
) : LessonInfoPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    private val dataLiveData: MutableLiveDataWithState<LessonInfoPageFragmentData>

    init {
        savedStateHandle.get<LessonInfoPageFragmentArguments>("args").let { args ->
            dataLiveData = MutableLiveDataWithState(
                initialValue = LessonInfoPageFragmentData(
                    a = 1
                )
            )
        }
    }

    override fun goBack() {
        // todo
    }
}