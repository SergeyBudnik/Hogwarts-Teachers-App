package com.bdev.hengschoolteacher.ui.views.branded

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.adapters.TextChangeAdapter
import kotlinx.android.synthetic.main.view_branded_header_search.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_branded_header_search)
open class BrandedHeaderSearchView : RelativeLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    @AfterViews
    fun init() {
        visibility = View.GONE

        brandedHeaderSearchCancelView.setOnClickListener {
            brandedHeaderSearchView.text.clear()

            visibility = View.GONE
        }
    }

    fun show() {
        visibility = View.VISIBLE

        brandedHeaderSearchView.requestFocus()
    }

    fun addOnTextChangeListener(listener: (String) -> Unit) {
        brandedHeaderSearchView.addTextChangedListener(object: TextChangeAdapter() {
            override fun onTextChanged(value: String) {
                listener.invoke(value)
            }
        })
    }
}
