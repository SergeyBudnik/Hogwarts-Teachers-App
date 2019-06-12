package com.bdev.hengschoolteacher.ui.views.branded

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
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
        brandedSecondaryHeaderItemTitleView.text = title

        setActive(active)
    }

    fun setIcon(iconId: Int, colorId: Int) {
        brandedSecondaryHeaderItemIconView.visibility = View.VISIBLE

        brandedSecondaryHeaderItemIconView.setImageDrawable(
                resources.getDrawable(iconId)
        )

        brandedSecondaryHeaderItemIconView.setColorFilter(
                resources.getColor(colorId),
                PorterDuff.Mode.SRC_IN
        )
    }

    fun setActive(active: Boolean) {
        brandedSecondaryHeaderItemTitleView.setTextColor(resources.getColor(if (active) {
            R.color.fill_text_basic
        } else {
            R.color.fill_text_basic_action_link
        }))

        activeMarkView.setBackgroundColor(resources.getColor(if (active) {
            R.color.fill_accent_strong
        } else {
            R.color.transparent
        }))
    }
}
