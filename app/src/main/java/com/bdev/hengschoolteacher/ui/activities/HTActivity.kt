package com.bdev.hengschoolteacher.ui.activities

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.bdev.hengschoolteacher.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HTActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ht)

        val viewModel = getViewModel()

        viewModel.getNavCommandEventsQueueLiveData().observe(this) { events ->
            val navController = getNavHostFragment().navController

            events.drainEvents().forEach { navCommand ->
                if (navCommand.quitApp) {
                    finishAffinity()
                } else {
                    when {
                        navCommand.closeCurrent -> {
                            navController.navigateUp()
                        }
                        navCommand.closeAll -> {
                            while (navController.navigateUp()) { /* Do nothing */ }
                        }
                    }

                    navCommand.navDir?.let { navDir ->
                        navController.navigate(navDir)
                    }
                }
            }
        }

        viewModel.onCreate()
    }

    private fun getNavHostFragment(): NavHostFragment =
        supportFragmentManager.findFragmentById(R.id.htNavHostFragment) as NavHostFragment

    private fun getViewModel(): HTActivityViewModel =
        ViewModelProvider(this).get(HTActivityViewModelImpl::class.java)
}