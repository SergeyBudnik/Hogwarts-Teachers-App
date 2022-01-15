package com.bdev.hengschoolteacher.ui.views.branded

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.views.common.CommonFontableEditTextView
import kotlinx.android.synthetic.main.view_branded_edit_text.view.*

class BrandedEditTextView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    enum class Style(val id: Int) {
        TEXT(1), PASSWORD(2);

        companion object {
            fun findById(id: Int): Style? {
                return values().find { it.id == id }
            }
        }
    }

    init {
        View.inflate(context, R.layout.view_branded_edit_text, this)
    }

    private val label: String
    private val hint: String
    private val style: Style

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BrandedEditTextView, 0, 0)

        try {
            val labelStyleable = R.styleable.BrandedEditTextView_branded_edit_text_label
            val hintStyleable = R.styleable.BrandedEditTextView_branded_edit_text_hint
            val styleStyleable = R.styleable.BrandedEditTextView_branded_edit_text_type

            label = ta.getString(labelStyleable) ?: ""
            hint = ta.getString(hintStyleable) ?: ""
            style = Style.findById(ta.getInteger(styleStyleable, -1)) ?: Style.TEXT
        } finally {
            ta.recycle()
        }

        brandedEditTextLabelView.text = label
        brandedEditTextControlView.hint = hint

        when (style) {
            Style.TEXT -> setTextStyle()
            Style.PASSWORD -> setPasswordStyle()
        }
    }

    fun getText(): String {
        return brandedEditTextControlView.text.toString()
    }

    private fun setTextStyle() {
        brandedEditTextControlView.inputType = InputType.TYPE_CLASS_TEXT
        brandedEditTextControlView.setFont(CommonFontableEditTextView.AppFont.REGULAR)
    }

    private fun setPasswordStyle() {
        brandedEditTextControlView.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        brandedEditTextControlView.setFont(CommonFontableEditTextView.AppFont.REGULAR)
    }
}
