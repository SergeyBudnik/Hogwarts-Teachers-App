package com.bdev.hengschoolteacher.ui.views.app

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import kotlinx.android.synthetic.main.view_app_header.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_app_header)
open class AppHeaderView(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private val title: String
    private val leftIconId: Int
    private val rightIconId: Int

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.AppHeaderView, 0, 0)

        try {
            title = ta.getString(R.styleable.AppHeaderView_title)
            leftIconId = ta.getResourceId(R.styleable.AppHeaderView_leftIcon, -1)
            rightIconId = ta.getResourceId(R.styleable.AppHeaderView_rightIcon, -1)
        } finally {
            ta.recycle()
        }
    }

    @AfterViews
    fun init() {
        titleView.text = title

        if (leftIconId != -1) { leftActionButtonView.setImageDrawable(context.getDrawable(leftIconId)) }
        if (rightIconId != -1) { rightActionButtonView.setImageDrawable(context.getDrawable(rightIconId)) }
    }

    fun setLeftButtonAction(action: () -> Unit): AppHeaderView {
        leftActionButtonView.setOnClickListener { action.invoke() }

        return this
    }

    fun setLeftButtonColor(color: Int): AppHeaderView {
        leftActionButtonView.setColorFilter(color, PorterDuff.Mode.SRC_IN)

        return this
    }

    fun setRightButtonAction(action: () -> Unit): AppHeaderView {
        rightActionButtonView.setOnClickListener { action.invoke() }

        return this
    }

    fun setRightButtonColor(color: Int): AppHeaderView {
        rightActionButtonView.setColorFilter(color, PorterDuff.Mode.SRC_IN)

        return this
    }
}
