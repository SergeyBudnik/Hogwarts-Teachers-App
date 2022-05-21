package com.bdev.hengschoolteacher.ui.fragments.student.header

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.data.common.NullableMutableLiveDataWithState
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModel
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.fragments.student.header.data.StudentHeaderFragmentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface StudentHeaderFragmentViewModel : BaseFragmentViewModel {
    fun getDataLiveData(): LiveData<StudentHeaderFragmentData>

    fun init(login: String, item: StudentHeaderFragmentItem)
}

@HiltViewModel
class StudentHeaderFragmentViewModelImpl @Inject constructor(
): StudentHeaderFragmentViewModel, BaseFragmentViewModelImpl() {
    private val dataLiveData = NullableMutableLiveDataWithState<StudentHeaderFragmentData>(
        initialValue = null
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun init(login: String, item: StudentHeaderFragmentItem) {
        dataLiveData.updateValue {
            StudentHeaderFragmentData(
                login = login,
                item = item
            )
        }
    }
}