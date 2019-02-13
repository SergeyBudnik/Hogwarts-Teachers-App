package com.bdev.hengschoolteacher.ui.views.app.common

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.DayOfWeek
import kotlinx.android.synthetic.main.view_list_item_day.view.*
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_list_item_day)
open class ListDayItemView : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(dayOfWeek: DayOfWeek): ListDayItemView {
        dayView.text = context.getString(dayOfWeek.shortNameId)

        return this
    }
}
