package com.bdev.hengschoolteacher.ui.views.branded

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.resources.AppResources
import kotlinx.android.synthetic.main.view_branded_action_button.view.*

class BrandedActionButtonView : RelativeLayout {
    init {
        View.inflate(context, R.layout.view_branded_action_button, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun setButtonText(text: String) {
        brandedActionButtonTitleView.text = text
    }

    fun hideButtonIcon() {
        brandedActionButtonIconView.visibility = View.GONE

        brandedActionButtonProgressView.clearAnimation()
        brandedActionButtonProgressView.visibility = View.GONE
    }

    fun setButtonIcon(iconId: Int, colorId: Int) {
        brandedActionButtonIconView.visibility = View.VISIBLE
        brandedActionButtonIconView.setImageDrawable(
                AppResources.getDrawable(
                        context = context,
                        drawableId = iconId
                )
        )
        brandedActionButtonIconView.setColorFilter(
                AppResources.getColor(
                        context = context,
                        colorId = colorId
                )
        )

        brandedActionButtonProgressView.clearAnimation()
        brandedActionButtonProgressView.visibility = View.GONE
    }

    fun setButtonInProgressIcon() {
        brandedActionButtonIconView.visibility = View.GONE

        brandedActionButtonProgressView.visibility = View.VISIBLE
        brandedActionButtonProgressView.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.spinner)
        )
    }
}
