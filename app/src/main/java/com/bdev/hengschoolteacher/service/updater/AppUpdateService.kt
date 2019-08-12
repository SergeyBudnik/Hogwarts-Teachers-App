package com.bdev.hengschoolteacher.service.updater

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import org.androidannotations.annotations.EBean
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.objects.Update
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.enums.UpdateFrom
import org.androidannotations.annotations.RootContext

data class AppUpdateInfo(val version: String)

interface AppUpdateListener {
    fun onUpdate(appUpdateInfo: AppUpdateInfo)
}

@EBean(scope = EBean.Scope.Singleton)
open class AppUpdateService {
    @RootContext
    lateinit var context: Context

    private var appUpdateListener: AppUpdateListener? = null
    private var appUpdateInfo: AppUpdateInfo? = null

    fun setUpdateListener(appUpdateListener: AppUpdateListener) {
        this.appUpdateListener = appUpdateListener

        notifyUpdate()
    }

    fun clearUpdateListener() {
        this.appUpdateListener = null
    }

    fun setUpdateHandled() {
        this.appUpdateInfo = null
    }

    fun goToGooglePlay() {
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

    fun enqueueUpdate() {
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
