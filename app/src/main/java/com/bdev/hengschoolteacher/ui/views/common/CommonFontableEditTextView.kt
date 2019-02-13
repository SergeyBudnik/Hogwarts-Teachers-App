package com.bdev.hengschoolteacher.ui.views.common

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.EditText
import com.bdev.hengschoolteacher.R

class CommonFontableEditTextView(context: Context, attrs: AttributeSet) : EditText(context, attrs) {
    enum class AppFont(
            val index: Int,
            val fontName: String
    ) {
        REGULAR(1, "fonts/sourcesanspro.ttf"),
        LIGHT(2, "fonts/sourcesansprolight.ttf"),
        BOLD(3, "fonts/sourcesansprosemibold.ttf");

        companion object {
            fun findByIndex(index: Int): AppFont {
                return values().find { it.index == index } ?: throw RuntimeException()
            }
        }
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CommonFontableEditTextView, 0, 0)

        try {
            val font = AppFont.findByIndex(ta.getInt(R.styleable.CommonFontableEditTextView_app_edit_text_font, 1))

            setFont(font)
        } finally {
            ta.recycle()
        }
    }

    fun setFont(font: AppFont) {
        typeface = Typeface.createFromAsset(context.assets, font.fontName)
    }
}
