package com.bdev.hengschoolteacher.ui.resources

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.res.ResourcesCompat
import com.bdev.hengschoolteacher.R
import java.lang.RuntimeException

object AppResources {
    fun getDrawable(context: Context, @DrawableRes drawableId: Int): Drawable {
        return ResourcesCompat.getDrawable(
                context.resources,
                drawableId,
                getTheme(context)
        ) ?: throw RuntimeException("The requested drawable by id '$drawableId' does not exit")
    }

    fun getColor(context: Context, @ColorRes colorId: Int): Int {
        return ResourcesCompat.getColor(
                context.resources,
                colorId,
                getTheme(context)
        )
    }

    private fun getTheme(context: Context): Resources.Theme {
        return ContextThemeWrapper(context, R.style.AppTheme).theme
    }
}