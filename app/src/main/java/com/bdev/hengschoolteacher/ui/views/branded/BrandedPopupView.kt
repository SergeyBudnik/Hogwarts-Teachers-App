package com.bdev.hengschoolteacher.ui.views.branded

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.bdev.hengschoolteacher.R
import kotlinx.android.synthetic.main.view_branded_popup.view.*

data class BrandedPopupInfo(
        val title: String,
        val text: String,
        val buttons: List<BrandedPopupButtonInfo>
)

enum class BrandedPopupButtonStyle {
    PRIMARY, DEFAULT
}

data class BrandedPopupButtonInfo(
        val text: String,
        val action: () -> Unit,
        val style: BrandedPopupButtonStyle
)

class BrandedPopupView : RelativeLayout {
    init {
        View.inflate(context, R.layout.view_branded_popup, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun show(brandedPopupInfo: BrandedPopupInfo) {
        visibility = View.VISIBLE

        brandedPopupShadowView.setOnClickListener {
            hide()
        }

        brandedPopupTitleView.text = brandedPopupInfo.title
        brandedPopupTextView.text = brandedPopupInfo.text

        setButton(brandedPopupButton1View, brandedPopupInfo, 0)
        setButton(brandedPopupButton2View, brandedPopupInfo, 1)
        setButton(brandedPopupButton3View, brandedPopupInfo, 2)
    }

    private fun hide() {
        visibility = View.GONE
    }

    private fun setButton(buttonView: TextView, brandedPopupInfo: BrandedPopupInfo, index: Int) {
        val brandedPopupButtonInfo =
                if (brandedPopupInfo.buttons.size <= index) {
                    null
                } else {
                    brandedPopupInfo.buttons[index]
                }

        buttonView.visibility = if (brandedPopupButtonInfo != null) {
            buttonView.text = brandedPopupButtonInfo.text
            buttonView.setOnClickListener {
                brandedPopupButtonInfo.action.invoke()

                hide()
            }

            buttonView.typeface = when (brandedPopupButtonInfo.style) {
                BrandedPopupButtonStyle.PRIMARY -> Typeface.DEFAULT_BOLD
                BrandedPopupButtonStyle.DEFAULT -> Typeface.DEFAULT
            }

            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
