package com.bdev.hengschoolteacher.ui.fragments.app_menu.views

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.resources.AppResources
import kotlinx.android.synthetic.main.view_app_menu_row.view.*

class AppMenuRowView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private val itemName: String
    private val itemIcon: Int

    init {
        inflate(context, R.layout.view_app_menu_row, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.AppMenuRowView, 0, 0)

        try {
            itemIcon = ta.getResourceId(R.styleable.AppMenuRowView_menuItemIcon, 0)
            itemName = ta.getString(R.styleable.AppMenuRowView_menuItemName) ?: ""
        } finally {
            ta.recycle()
        }

        appMenuItemIconView.setImageDrawable(
            AppResources.getDrawable(
                context = context,
                drawableId = itemIcon
            )
        )

        appMenuItemNameView.text = itemName
    }

    fun bind(isCurrent: Boolean, hasAlerts: Boolean) {
        initActiveMarkerColor(isCurrent = isCurrent, hasAlerts = hasAlerts)
        initIconColor(isCurrent = isCurrent, hasAlerts = hasAlerts)
        initNameColor(isCurrent = isCurrent, hasAlerts = hasAlerts)
    }

    private fun initActiveMarkerColor(isCurrent: Boolean, hasAlerts: Boolean) {
        appMenuItemActiveMarkerView.setBackgroundColor(
            AppResources.getColor(
                context = context,
                colorId = when {
                    isCurrent -> {
                        if (hasAlerts) {
                            R.color.fill_text_basic_negative
                        } else {
                            R.color.fill_text_basic_accent
                        }
                    }
                    else -> {
                        R.color.transparent
                    }
                }
            )
        )
    }

    private fun initIconColor(isCurrent: Boolean, hasAlerts: Boolean) {
        appMenuItemIconView.setColorFilter(
            AppResources.getColor(
                context = context,
                colorId = when {
                    isCurrent -> {
                        R.color.fill_text_basic_accent
                    }
                    hasAlerts -> {
                        R.color.fill_text_basic_negative
                    }
                    else -> {
                        R.color.fill_text_basic
                    }
                }
            ),
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun initNameColor(isCurrent: Boolean, hasAlerts: Boolean) {
        appMenuItemNameView.setTextColor(
            AppResources.getColor(
                context = context,
                colorId = when {
                    isCurrent -> {
                        R.color.fill_text_basic_accent
                    }
                    hasAlerts -> {
                        R.color.fill_text_basic_negative
                    }
                    else -> {
                        R.color.fill_text_basic
                    }
                }
            )
        )
    }
}