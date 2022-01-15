package com.bdev.hengschoolteacher.ui.views.branded

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.resources.AppResources
import kotlinx.android.synthetic.main.view_branded_secondary_header_item.view.*

class BrandedSecondaryHeaderItem(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private val title: String
    private val active: Boolean

    init {
        View.inflate(context, R.layout.view_branded_secondary_header_item, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.BrandedSecondaryHeaderItem, 0, 0)

        try {
            title = ta.getString(R.styleable.BrandedSecondaryHeaderItem_header_title) ?: ""
            active = ta.getBoolean(R.styleable.BrandedSecondaryHeaderItem_header_active, false)
        } finally {
            ta.recycle()
        }

        brandedSecondaryHeaderItemTitleView.text = title

        bind(active = active, hasAlert = false, clickAction = {})
    }

    fun bind(active: Boolean, hasAlert: Boolean, clickAction: () -> Unit) {
        brandedSecondaryHeaderItemTitleView.setTextColor(
                AppResources.getColor(
                        context = context,
                        colorId = when {
                            hasAlert -> {
                                R.color.fill_text_basic_negative
                            }
                            active -> {
                                R.color.fill_text_basic_accent
                            }
                            else -> {
                                R.color.fill_text_basic
                            }
                        }
                )
        )

        activeMarkView.setBackgroundColor(
                AppResources.getColor(
                        context = context,
                        colorId = when {
                            active -> {
                                if (hasAlert) {
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

        setOnClickListener { clickAction() }
    }
}
