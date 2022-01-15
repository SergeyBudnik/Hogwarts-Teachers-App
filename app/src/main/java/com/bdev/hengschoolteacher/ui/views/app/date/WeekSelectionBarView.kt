package com.bdev.hengschoolteacher.ui.views.app.date

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.utils.TimeUtils
import kotlinx.android.synthetic.main.view_week_selection_bar.view.*
import java.text.SimpleDateFormat
import java.util.*

class WeekSelectionBarView : RelativeLayout {
    companion object {
        private const val WEEK_LEFT_BORDER = -8
        private const val WEEK_RIGHT_BORDED = 0
        private const val WEEK_CENTER = 0
    }

    private var currentWeekIndex = WEEK_CENTER
    private var changeListener: (Int) -> Unit = { _ -> }

    init {
        View.inflate(context, R.layout.view_week_selection_bar, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun init(changeListener: (Int) -> Unit) {
        this.changeListener = changeListener

        update()

        weekSelectionBarWeekView.setOnClickListener {
            currentWeekIndex = WEEK_CENTER

            update()
        }

        weekSelectionBarPreviousWeekView.setOnClickListener {
            if (currentWeekIndex > WEEK_LEFT_BORDER) {
                currentWeekIndex--

                update()
            }
        }

        weekSelectionBarNextWeekView.setOnClickListener {
            if (currentWeekIndex < WEEK_RIGHT_BORDED) {
                currentWeekIndex++

                update()
            }
        }
    }

    private fun update() {
        changeListener.invoke(currentWeekIndex)

        updateWeekTitle()
        updateWeekRange()

        setLeftArrowEnabled()
        setRightArrowEnabled()
    }

    private fun updateWeekTitle() {
        weekSelectionBarWeekView.text = if (currentWeekIndex == 0) {
            "Текущая неделя"
        } else if (currentWeekIndex < 0) {
            "${-currentWeekIndex} недель назад"
        } else {
            ""
        }
    }

    private fun updateWeekRange() {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)

        val startTime = TimeUtils().getWeekStart(currentWeekIndex)
        val finishTime = TimeUtils().getWeekFinish(currentWeekIndex)

        val startTimeString = sdf.format(Date(startTime))
        val finishTimeString = sdf.format(Date(finishTime))

        weekSelectionBarDateRangeView.text = "$startTimeString - $finishTimeString"
    }

    private fun setLeftArrowEnabled() {
        weekSelectionBarPreviousWeekView.alpha = if (currentWeekIndex == WEEK_LEFT_BORDER) { 0.2f } else { 1.0f }
    }

    private fun setRightArrowEnabled() {
        weekSelectionBarNextWeekView.alpha = if (currentWeekIndex == WEEK_RIGHT_BORDED) { 0.2f } else { 1.0f }
    }
}
