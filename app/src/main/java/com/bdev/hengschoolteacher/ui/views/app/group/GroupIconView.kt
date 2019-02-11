package com.bdev.hengschoolteacher.ui.views.app.group

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.ImageView
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Group

open class GroupIconView : ImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        setImageDrawable(resources.getDrawable(R.drawable.ic_lesson_time))
    }

    fun bind(group: Group) {
        setColorFilter(Color.parseColor(group.color), PorterDuff.Mode.SRC_IN)
    }
}
