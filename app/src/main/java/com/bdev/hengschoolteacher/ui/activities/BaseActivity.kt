package com.bdev.hengschoolteacher.ui.activities

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.bdev.hengschoolteacher.service.updater.AppUpdateInfo
import com.bdev.hengschoolteacher.service.updater.AppUpdateListener
import com.bdev.hengschoolteacher.service.updater.AppUpdateService
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.branded.BrandedPopupButtonInfo
import com.bdev.hengschoolteacher.ui.views.branded.BrandedPopupButtonStyle
import com.bdev.hengschoolteacher.ui.views.branded.BrandedPopupInfo
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity
abstract class BaseActivity : FragmentActivity(), AppUpdateListener {
    @Bean
    lateinit var appUpdateService: AppUpdateService

    override fun onStart() {
        super.onStart()

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
    }

    override fun onResume() {
        super.onResume()

        appUpdateService.setUpdateListener(this)
    }

    override fun onPause() {
        super.onPause()

        appUpdateService.clearUpdateListener()
    }

    override fun onUpdate(appUpdateInfo: AppUpdateInfo) {
        getAppLayoutView()?.getPopupView()?.let { popup ->
            appUpdateService.setUpdateHandled()

            popup.show(
                    BrandedPopupInfo(
                            title = "Доступна новая версия (${appUpdateInfo.version})",
                            text = "После обновления Вам будут доступны последние улучшения и исправления. Для корректной работы приложения Вам необходимо обновиться.",
                            buttons = listOf(
                                    BrandedPopupButtonInfo(
                                            text = "Пропустить",
                                            action = {},
                                            style = BrandedPopupButtonStyle.DEFAULT
                                    ),
                                    BrandedPopupButtonInfo(
                                            text = "Обновиться",
                                            action = { appUpdateService.goToGooglePlay() },
                                            style = BrandedPopupButtonStyle.PRIMARY
                                    )
                            )
                    )
            )
        }
    }

    abstract fun getAppLayoutView(): AppLayoutView?
}
