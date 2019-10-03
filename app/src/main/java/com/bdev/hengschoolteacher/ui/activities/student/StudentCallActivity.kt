package com.bdev.hengschoolteacher.ui.activities.student

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.PhoneUtils
import kotlinx.android.synthetic.main.view_item_student_phone.view.*
import org.androidannotations.annotations.EViewGroup

// ToDo: move
@EViewGroup(R.layout.view_item_student_phone)
open class StudentPhoneItemView : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(phone: String) : StudentPhoneItemView {
        studentPhoneView.text = phone

        setOnClickListener { PhoneUtils.call(
                context as BaseActivity,
                phone
        ) }

        return this
    }
}