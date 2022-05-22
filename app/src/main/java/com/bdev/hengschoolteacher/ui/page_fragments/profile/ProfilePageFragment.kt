package com.bdev.hengschoolteacher.ui.page_fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import com.bdev.hengschoolteacher.ui.page_fragments.common.content.BaseContentPageFragment
import com.bdev.hengschoolteacher.ui.views.app.AppHeaderView
import com.bdev.hengschoolteacher.ui.views.app.root.HtPageRootView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.page_fragment_profile.*

@AndroidEntryPoint
class ProfilePageFragment : BaseContentPageFragment<ProfilePageFragmentViewModel>(
    menuItem = AppMenuItem.PROFILE
) {
    override fun provideViewModel() =
        ViewModelProvider(this).get(ProfilePageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.page_fragment_profile, container, false)

    override fun getRootView(): HtPageRootView = profileRootView
    override fun getPrimaryHeaderView(): AppHeaderView = profilePrimaryHeaderView
}