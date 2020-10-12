package com.bdev.hengschoolteacher.ui.views.branded

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import kotlinx.android.synthetic.main.view_branded_secondary_header_item.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_branded_secondary_header_item)
open class BrandedSecondaryHeaderItem(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private val title: String
    private val active: Boolean

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BrandedSecondaryHeaderItem, 0, 0)

        try {
            title = ta.getString(R.styleable.BrandedSecondaryHeaderItem_header_title) ?: ""
            active = ta.getBoolean(R.styleable.BrandedSecondaryHeaderItem_header_active, false)
        } finally {
            ta.recycle()
        }
    }

    @AfterViews
    fun init() {
        brandedSecondaryHeaderItemTitleView.text = title

        bind(active = active, hasAlert = false, clickAction = {})
    }

    fun bind(active: Boolean, hasAlert: Boolean, clickAction: () -> Unit) {
        brandedSecondaryHeaderItemTitleView.setTextColor(resources.getColor(when {
            hasAlert -> {
                R.color.fill_text_basic_negative
            }
            active -> {
                R.color.fill_text_basic_accent
            }
            else -> {
                R.color.fill_text_basic
            }
        }))

        activeMarkView.setBackgroundColor(resources.getColor(when {
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
        }))

        setOnClickListener { clickAction() }
    }
}
