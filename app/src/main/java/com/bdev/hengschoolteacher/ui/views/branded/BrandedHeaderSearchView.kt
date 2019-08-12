package com.bdev.hengschoolteacher.ui.views.branded

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.adapters.TextChangeAdapter
import com.bdev.hengschoolteacher.ui.utils.KeyboardUtils
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
            hide()
        }

        brandedHeaderSearchView.setOnFocusChangeListener { _, focus ->
            if (focus) {
                KeyboardUtils.showKeyboard(context as BaseActivity)
            }
        }
    }

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
        KeyboardUtils.hideKeyboard(context as BaseActivity)

        brandedHeaderSearchView.text.clear()

        visibility = View.GONE
    }
}
