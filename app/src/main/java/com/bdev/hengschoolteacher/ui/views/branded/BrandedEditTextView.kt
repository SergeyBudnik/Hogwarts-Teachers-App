package com.bdev.hengschoolteacher.ui.views.branded

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.views.common.CommonFontableEditTextView
import kotlinx.android.synthetic.main.view_branded_edit_text.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_branded_edit_text)
open class BrandedEditTextView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    enum class Style(val id: Int) {
        TEXT(1), PASSWORD(2);

        companion object {
            fun findById(id: Int): BrandedEditTextView.Style? {
                return values().find { it.id == id }
            }
        }
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

            label = ta.getString(labelStyleable)
            hint = ta.getString(hintStyleable)
            style = BrandedEditTextView.Style.findById(ta.getInteger(styleStyleable, -1)) ?: BrandedEditTextView.Style.TEXT
        } finally {
            ta.recycle()
        }
    }

    @AfterViews
    fun init() {
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
