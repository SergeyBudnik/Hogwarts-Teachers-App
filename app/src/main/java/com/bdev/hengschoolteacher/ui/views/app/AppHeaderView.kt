package com.bdev.hengschoolteacher.ui.views.app

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.resources.AppResources
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import kotlinx.android.synthetic.main.view_app_header.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup

class AppHeaderView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private val title: String
    private val leftIconId: Int
    private val firstRightIconId: Int
    private val secondRightIconId: Int
    private val thirdRightIconId: Int

    init {
        View.inflate(context, R.layout.view_app_header, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.AppHeaderView, 0, 0)

        try {
            title = ta.getString(R.styleable.AppHeaderView_title) ?: ""
            leftIconId = ta.getResourceId(R.styleable.AppHeaderView_leftIcon, -1)
            firstRightIconId = ta.getResourceId(R.styleable.AppHeaderView_firstRightIcon, -1)
            secondRightIconId = ta.getResourceId(R.styleable.AppHeaderView_secondRightIcon, -1)
            thirdRightIconId = ta.getResourceId(R.styleable.AppHeaderView_thirdRightIcon, -1)
        } finally {
            ta.recycle()
        }

        titleView.text = title

        setActionButton(leftActionButtonView, leftIconId)
        setActionButton(firstRightActionButtonView, firstRightIconId)
        setActionButton(secondRightActionButtonView, secondRightIconId)
        setActionButton(thirdRightActionButtonView, thirdRightIconId)
    }

    private fun setActionButton(actionButtonView: ImageView, drawableId: Int) {
        actionButtonView.visibility = visibleElseGone(visible = (drawableId != -1))

        if (drawableId != -1) {
            actionButtonView.setImageDrawable(
                    AppResources.getDrawable(
                            context = context,
                            drawableId = drawableId
                    )
            )
        }
    }

    fun setTitle(title: String): AppHeaderView {
        titleView.text = title

        return this
    }

    fun setLeftButtonAction(action: () -> Unit): AppHeaderView {
        leftActionButtonView.setOnClickListener { action.invoke() }

        return this
    }

    fun getFirstButtonHandler() = AppHeaderIconHandler(context = context, view = firstRightActionButtonView)
    fun getSecondButtonHandler() = AppHeaderIconHandler(context = context, view = secondRightActionButtonView)
    fun getThirdButtonHandler() = AppHeaderIconHandler(context = context, view = thirdRightActionButtonView)
}

class AppHeaderIconHandler internal constructor(
        private val context: Context,
        private val view: ImageView
) {
    fun setAction(action: () -> Unit): AppHeaderIconHandler {
        view.setOnClickListener { action() }

        return this
    }

    fun setToggled(toggled: Boolean): AppHeaderIconHandler {
        val color = AppResources.getColor(
                context = context,
                colorId = if (toggled) {
                    R.color.fill_text_basic_accent
                } else {
                    R.color.fill_text_basic
                }
        )

        view.setColorFilter(color, PorterDuff.Mode.SRC_IN)

        return this
    }
}
