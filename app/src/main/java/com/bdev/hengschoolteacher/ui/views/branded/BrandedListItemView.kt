package com.bdev.hengschoolteacher.ui.views.branded

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import kotlinx.android.synthetic.main.view_branded_list_item.view.*

class BrandedListItemView : RelativeLayout {
    private val customViews: MutableSet<View> = HashSet()
    private val customViewsWithParams: MutableSet<Pair<View, ViewGroup.LayoutParams>> = HashSet()

    init {
        View.inflate(context, R.layout.view_branded_list_item, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onFinishInflate() {
        super.onFinishInflate()

        customViews.forEach { brandedListItemContainerView.addView(it) }
        customViewsWithParams.forEach { brandedListItemContainerView.addView(it.first, it.second) }
    }

    override fun addView(child: View) {
        if (isCustomView(child)) {
            customViews.add(child)
        } else {
            super.addView(child)
        }
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        if (isCustomView(child)) {
            customViewsWithParams.add(Pair(child, params))
        } else {
            super.addView(child, params)
        }
    }

    private fun isCustomView(view: View): Boolean {
        return !listOf(R.id.brandedListItemContainerView).contains(view.id)
    }
}
