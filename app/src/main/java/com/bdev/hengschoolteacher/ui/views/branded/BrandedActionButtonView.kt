package com.bdev.hengschoolteacher.ui.views.branded

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import kotlinx.android.synthetic.main.view_branded_action_button.view.*
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.res.AnimationRes

@EViewGroup(R.layout.view_branded_action_button)
open class BrandedActionButtonView : RelativeLayout {
    @AnimationRes(R.anim.spinner)
    lateinit var spinnerAnimation: Animation

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
        brandedActionButtonIconView.setImageDrawable(resources.getDrawable(iconId))
        brandedActionButtonIconView.setColorFilter(resources.getColor(colorId))

        brandedActionButtonProgressView.clearAnimation()
        brandedActionButtonProgressView.visibility = View.GONE
    }

    fun setButtonInProgressIcon() {
        brandedActionButtonIconView.visibility = View.GONE

        brandedActionButtonProgressView.visibility = View.VISIBLE
        brandedActionButtonProgressView.startAnimation(spinnerAnimation)
    }
}
