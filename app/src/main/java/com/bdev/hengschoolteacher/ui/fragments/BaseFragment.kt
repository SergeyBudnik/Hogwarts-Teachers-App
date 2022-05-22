package com.bdev.hengschoolteacher.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.ui.activities.HTActivity
import com.bdev.hengschoolteacher.ui.activities.HTActivityViewModel
import com.bdev.hengschoolteacher.ui.activities.HTActivityViewModelImpl

abstract class BaseFragment<ViewModelType : BaseFragmentViewModel> : Fragment() {
    protected lateinit var fragmentViewModel: ViewModelType private set

    private lateinit var activityViewModel: HTActivityViewModel

    abstract fun provideViewModel(): ViewModelType

    fun activity(): HTActivity = requireActivity() as HTActivity

    open fun doOnViewCreated() {}

    @SuppressLint("FragmentLiveDataObserve")
    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityViewModel = provideActivityViewModel()
        fragmentViewModel = provideViewModel()

        fragmentViewModel.init(activityViewModel = activityViewModel)

        fragmentViewModel.getNavCommandEventLiveData().observe(this) { navCommandsEvents ->
            navCommandsEvents.drainEvents().firstOrNull()?.let { navCommand ->
                activityViewModel.navigate(navCommand)
            }
        }

        fragmentViewModel.onCreate(fragment = this)

        doOnViewCreated()
    }

    override fun onStart() {
        super.onStart()

        fragmentViewModel.onStart(fragment = this)
    }

    override fun onResume() {
        super.onResume()

        fragmentViewModel.onResume(fragment = this)
    }

    override fun onPause() {
        super.onPause()

        fragmentViewModel.onPause(fragment = this)
    }

    override fun onStop() {
        super.onStop()

        fragmentViewModel.onStop(fragment = this)
    }

    private fun provideActivityViewModel(): HTActivityViewModel =
        ViewModelProvider(activity()).get(HTActivityViewModelImpl::class.java)
}