package com.bdev.hengschoolteacher.interactors.updater

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.github.javiersantos.appupdater.objects.Update
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

data class AppUpdateInfo(val version: String)

interface AppUpdateListener {
    fun onUpdate(appUpdateInfo: AppUpdateInfo)
}

interface AppUpdateService {
    fun setUpdateListener(appUpdateListener: AppUpdateListener)
    fun clearUpdateListener()
    fun setUpdateHandled()
    fun goToGooglePlay()
    fun enqueueUpdate()
}

class AppUpdateServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
): AppUpdateService {
    private var appUpdateListener: AppUpdateListener? = null
    private var appUpdateInfo: AppUpdateInfo? = null

    override fun setUpdateListener(appUpdateListener: AppUpdateListener) {
        this.appUpdateListener = appUpdateListener

        notifyUpdate()
    }

    override fun clearUpdateListener() {
        this.appUpdateListener = null
    }

    override fun setUpdateHandled() {
        this.appUpdateInfo = null
    }

    override fun goToGooglePlay() {
        val appPackageName = context.packageName

        val googlePlayIntentProvider: (String) -> Intent = { uri ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

            intent.flags = FLAG_ACTIVITY_NEW_TASK

            intent
        }

        try {
            context.startActivity(
                    googlePlayIntentProvider.invoke("market://details?id=$appPackageName")
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                    googlePlayIntentProvider.invoke("https://play.google.com/store/apps/details?id=$appPackageName")
            )
        }
    }

    override fun enqueueUpdate() {
        AppUpdaterUtils(context)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .withListener(object : AppUpdaterUtils.UpdateListener {
                    override fun onSuccess(update: Update, isUpdateAvailable: Boolean) {
                        if (isUpdateAvailable) {
                            appUpdateInfo = AppUpdateInfo(version = update.latestVersion)

                            notifyUpdate()
                        }
                    }

                    override fun onFailed(error: AppUpdaterError) {
                        /* Do nothing */
                    }
                }).start()
    }

    private fun notifyUpdate() {
        appUpdateListener?.let { appUpdateListener ->
            appUpdateInfo?.let { appUpdateInfo ->
                appUpdateListener.onUpdate(appUpdateInfo)
            }
        }
    }
}
