package com.bdev.hengschoolteacher.ui.fragments.student.header

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.BaseFragment
import com.bdev.hengschoolteacher.ui.fragments.student.header.data.StudentHeaderFragmentItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_student_header.*

@AndroidEntryPoint
class StudentHeaderFragment : BaseFragment<StudentHeaderFragmentViewModel>() {
    override fun provideViewModel() =
        ViewModelProvider(this).get(StudentHeaderFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_student_header, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            updateView(data = data)
        }
    }

    fun init(login: String, item: StudentHeaderFragmentItem) {
        fragmentViewModel.init(
            login = login,
            item = item
        )
    }

    private fun updateView(data: StudentHeaderFragmentData) {
        studentHeaderView.bind(
            data = data,
            navCommandHandler = { navCommand ->
                fragmentViewModel.navigate(navCommand = navCommand)
            }
        )
    }
}