package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.teachers.views

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.fragments.content.teachers.data.MonitoringTeachersItemData
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import kotlinx.android.synthetic.main.view_monitoring_teachers_item.view.*

class MonitoringTeachersItemView : RelativeLayout {
    init {
        inflate(context, R.layout.view_monitoring_teachers_item, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(model: MonitoringTeachersItemData): MonitoringTeachersItemView {
        monitoringTeachersItemNameView.text = model.staffMember.person.name

        monitoringTeachersItemAlertView.visibility = visibleElseGone(
            visible = model.hasAlerts
        )

        return this
    }
}