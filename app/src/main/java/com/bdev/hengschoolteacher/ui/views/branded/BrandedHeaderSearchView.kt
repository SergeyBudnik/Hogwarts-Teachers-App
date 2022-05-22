package com.bdev.hengschoolteacher.ui.views.branded

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.adapters.TextChangeAdapter
import com.bdev.hengschoolteacher.ui.utils.KeyboardUtils
import kotlinx.android.synthetic.main.view_branded_header_search.view.*

class BrandedHeaderSearchView : RelativeLayout {
    init {
        View.inflate(context, R.layout.view_branded_header_search, this)

        visibility = View.GONE

        brandedHeaderSearchCancelView.setOnClickListener {
            hide()
        }

        brandedHeaderSearchView.setOnFocusChangeListener { _, focus ->
            if (focus) {
                // KeyboardUtils.showKeyboard(context as BasePageFragment)
            }
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun addOnTextChangeListener(listener: (String) -> Unit) {
        brandedHeaderSearchView.addTextChangedListener(object: TextChangeAdapter() {
            override fun onTextChanged(value: String) {
                listener.invoke(value)
            }
        })
    }

    fun show() {
        visibility = View.VISIBLE

        brandedHeaderSearchView.requestFocus()
    }

    private fun hide() {
        // KeyboardUtils.hideKeyboard(context as BasePageFragment)

        brandedHeaderSearchView.text.clear()

        visibility = View.GONE
    }
}
