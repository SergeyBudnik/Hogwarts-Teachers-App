package com.bdev.hengschoolteacher.ui.views.app

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.resources.AppResources
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import kotlinx.android.synthetic.main.view_app_header.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_app_header)
open class AppHeaderView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private val title: String
    private val leftIconId: Int
    private val firstRightIconId: Int
    private val secondRightIconId: Int

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.AppHeaderView, 0, 0)

        try {
            title = ta.getString(R.styleable.AppHeaderView_title) ?: ""
            leftIconId = ta.getResourceId(R.styleable.AppHeaderView_leftIcon, -1)
            firstRightIconId = ta.getResourceId(R.styleable.AppHeaderView_firstRightIcon, -1)
            secondRightIconId = ta.getResourceId(R.styleable.AppHeaderView_secondRightIcon, -1)
        } finally {
            ta.recycle()
        }
    }

    @AfterViews
    fun init() {
        titleView.text = title

        setActionButton(leftActionButtonView, leftIconId)
        setActionButton(firstRightActionButtonView, firstRightIconId)
        setActionButton(secondRightActionButtonView, secondRightIconId)
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

    fun setFirstRightButtonAction(action: () -> Unit): AppHeaderView {
        firstRightActionButtonView.setOnClickListener { action.invoke() }

        return this
    }

    fun setFirstRightButtonActive(active: Boolean): AppHeaderView {
        firstRightActionButtonView.setColorFilter(
                getButtonColor(active),
                PorterDuff.Mode.SRC_IN
        )

        return this
    }

    fun setSecondRightButtonAction(action: () -> Unit): AppHeaderView {
        secondRightActionButtonView.setOnClickListener { action.invoke() }

        return this
    }

    fun setSecondRightButtonActive(active: Boolean): AppHeaderView {
        secondRightActionButtonView.setColorFilter(
                getButtonColor(active),
                PorterDuff.Mode.SRC_IN
        )

        return this
    }

    private fun getButtonColor(active: Boolean): Int {
        return AppResources.getColor(
                context = context,
                colorId = if (active) {
                    R.color.fill_text_basic_accent
                } else {
                    R.color.fill_text_basic
                }
        )
    }
}
