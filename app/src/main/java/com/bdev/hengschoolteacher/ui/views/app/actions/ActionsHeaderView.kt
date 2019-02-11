package com.bdev.hengschoolteacher.ui.views.app.actions

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_actions_header)
open class ActionsHeaderView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}
