package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import com.bdev.hengschoolteacher.ui.page_fragments.common.content.BaseContentPageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.header.MonitoringTeacherHeaderFragment
import com.bdev.hengschoolteacher.ui.views.app.AppHeaderView
import com.bdev.hengschoolteacher.ui.views.app.root.HtPageRootView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.page_fragment_monitoring_teacher.*

@AndroidEntryPoint
class MonitoringTeacherPageFragment : BaseContentPageFragment<MonitoringTeacherPageFragmentViewModel>(
    menuItem = AppMenuItem.NONE
) {
    private val args: MonitoringTeacherPageFragmentArgs by navArgs()

    override fun provideViewModel() =
        ViewModelProvider(this).get(MonitoringTeacherPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.page_fragment_monitoring_teacher, container, false)

    override fun getRootView(): HtPageRootView = monitoringTeacherRootView
    override fun getPrimaryHeaderView(): AppHeaderView = monitoringTeacherPrimaryHeaderView

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        fragmentViewModel.init(args = args.args)

        monitoringTeacherSecondaryHeaderView.getFragment<MonitoringTeacherHeaderFragment>().bind(
            teacherLogin = args.args.teacherLogin
        )
    }
}