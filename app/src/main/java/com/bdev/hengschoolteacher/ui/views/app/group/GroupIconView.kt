package com.bdev.hengschoolteacher.ui.views.app.group

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.ui.resources.AppResources

class GroupIconView : AppCompatImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        setImageDrawable(
                AppResources.getDrawable(
                        context = context,
                        drawableId = R.drawable.ic_lesson_time
                )
        )
    }

    fun bind(group: Group) {
        setColorFilter(Color.parseColor(group.color), PorterDuff.Mode.SRC_IN)
    }
}
