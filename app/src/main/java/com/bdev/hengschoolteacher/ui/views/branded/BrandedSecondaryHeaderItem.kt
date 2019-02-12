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
            title = ta.getString(R.styleable.BrandedSecondaryHeaderItem_header_title)
            active = ta.getBoolean(R.styleable.BrandedSecondaryHeaderItem_header_active, false)
        } finally {
            ta.recycle()
        }
    }

    @AfterViews
    fun init() {
        titleView.text = title

        setActive(active)
    }

    fun setActive(active: Boolean) {
        titleView.setTextColor(resources.getColor(if (active) {
            R.color.fill_text_base
        } else {
            R.color.fill_text_action_link
        }))

        activeMarkView.setBackgroundColor(resources.getColor(if (active) {
            R.color.fill_text_base
        } else {
            R.color.transparent
        }))
    }
}
