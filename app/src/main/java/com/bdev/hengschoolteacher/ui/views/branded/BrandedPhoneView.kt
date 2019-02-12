package com.bdev.hengschoolteacher.ui.views.branded

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.PhoneUtils
import kotlinx.android.synthetic.main.view_branded_phone.view.*
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_branded_phone)
open class BrandedPhoneView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(phone: String) : BrandedPhoneView {
        brandedPhoneTextView.text = phone

        setOnClickListener { PhoneUtils.call(context as BaseActivity, phone) }

        return this
    }
}
