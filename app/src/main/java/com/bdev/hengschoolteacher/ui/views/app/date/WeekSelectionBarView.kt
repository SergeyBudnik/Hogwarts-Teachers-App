package com.bdev.hengschoolteacher.ui.views.app.date

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.utils.TimeUtils
import kotlinx.android.synthetic.main.view_week_selection_bar.view.*
import org.androidannotations.annotations.EViewGroup
import java.text.SimpleDateFormat
import java.util.*

@EViewGroup(R.layout.view_week_selection_bar)
open class WeekSelectionBarView : RelativeLayout {
    private var currentWeekIndex = 0
    private var changeListener: (Long, Long) -> Unit = { _, _ -> }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun init() {
        setTime()

        weekSelectionBarPreviousWeekView.setOnClickListener {
            currentWeekIndex--

            setTime()
        }

        weekSelectionBarNextWeekView.setOnClickListener {
            currentWeekIndex++

            setTime()
        }
    }

    fun setOnWeekChangedListener(changeListener: (Long, Long) -> Unit) {
        this.changeListener = changeListener
    }

    private fun setTime() {
        val startTime = TimeUtils().getWeekStart(currentWeekIndex)
        val finishTime = TimeUtils().getWeekFinish(currentWeekIndex)

        changeListener.invoke(startTime, finishTime)

        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)

        val startTimeString = sdf.format(Date(startTime))
        val finishTimeString = sdf.format(Date(finishTime))

        weekSelectionBarDateRangeView.text = "$startTimeString - $finishTimeString"
    }
}
