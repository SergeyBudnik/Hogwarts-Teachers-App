package com.bdev.hengschoolteacher.ui.views.branded

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.resources.AppResources
import kotlinx.android.synthetic.main.view_branded_button.view.*

class BrandedButtonView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    enum class Style(
            val id: Int,
            val backgroundId: Int,
            val backgroundColorId: Int,
            val textColorId: Int
    ) {
        PRIMARY(1, R.drawable.layout_button_filled, R.color.fill_accent_strong, R.color.fill_text_basic),
        DEFAULT(2, R.drawable.layout_button_linear, R.color.fill_text_basic_action_link, R.color.fill_text_basic_action_link);

        companion object {
            fun findById(id: Int): Style? {
                return values().find { it.id == id }
            }
        }
    }

    private var text: String
    private var style: Style

    init {
        View.inflate(context, R.layout.view_branded_button, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.BrandedButtonView, 0, 0)

        try {
            text = ta.getString(R.styleable.BrandedButtonView_button_text) ?: ""
            style = Style.findById(ta.getInteger(R.styleable.BrandedButtonView_button_style, -1)) ?: Style.PRIMARY
        } finally {
            ta.recycle()
        }

        doInit()
    }

    fun setText(text: String) {
        this.text = text

        doInit()
    }

    fun setStyle(style: Style) {
        this.style = style

        doInit()
    }

    private fun doInit() {
        brandedButtonView.setImageDrawable(
                AppResources.getDrawable(
                        context = context,
                        drawableId = style.backgroundId
                )
        )

        brandedButtonView.setColorFilter(
                AppResources.getColor(
                        context = context,
                        colorId = style.backgroundColorId
                ),
                PorterDuff.Mode.SRC_IN
        )

        brandedButtonTextView.text = text

        brandedButtonTextView.setTextColor(
                AppResources.getColor(
                        context = context,
                        colorId = style.textColorId
                )
        )
    }
}
