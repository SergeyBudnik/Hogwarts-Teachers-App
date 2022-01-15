package com.bdev.hengschoolteacher.ui.views.app.actions

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R

class ActionsHeaderView : LinearLayout {
    init {
        View.inflate(context, R.layout.view_actions_header, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}
