package com.bdev.hengschoolteacher.ui.views.app.dev

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_in_development)
open class InDevelopmentView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}
