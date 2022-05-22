package com.bdev.hengschoolteacher.ui.fragments.app_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.BaseFragment
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_app_menu.*

@AndroidEntryPoint
class AppMenuFragment : BaseFragment<AppMenuFragmentViewModel>() {
    override fun provideViewModel() =
        ViewModelProvider(this).get(AppMenuFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_app_menu, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            updateView(data = data)
        }
    }

    fun setCurrentItem(item: AppMenuItem) {
        fragmentViewModel.setItem(item = item)
    }

    private fun updateView(data: AppMenuFragmentData) {
        appMenuView.bind(
            item = data.item,
            me = data.me,
            hasProfileAlerts = data.hasProfileAlerts,
            hasMonitoringAlerts = data.hasMonitoringAlerts,
            navCommandHandler = { navCommand ->
                fragmentViewModel.navigate(navCommand = navCommand)
            }
        )
    }
}