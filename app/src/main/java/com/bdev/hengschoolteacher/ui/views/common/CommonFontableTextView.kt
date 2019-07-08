package com.bdev.hengschoolteacher.ui.views.common

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.bdev.hengschoolteacher.R

open class CommonFontableTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {
    private enum class AppFont(
            val index: Int,
            val fontName: String
    ) {
        REGULAR(1, "fonts/sourcesanspro.ttf"),
        LIGHT(2, "fonts/sourcesansprolight.ttf"),
        BOLD(3, "fonts/sourcesansprosemibold.ttf");

        companion object {
            fun findByIndex(index: Int): AppFont? {
                return values().find { it.index == index }
            }
        }
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CommonFontableTextView, 0, 0)

        try {
            val font = AppFont.findByIndex(ta.getInt(R.styleable.CommonFontableTextView_app_font, 1)) ?: throw RuntimeException()

            typeface = Typeface.createFromAsset(context.assets, font.fontName)
        } finally {
            ta.recycle()
        }
    }
}
