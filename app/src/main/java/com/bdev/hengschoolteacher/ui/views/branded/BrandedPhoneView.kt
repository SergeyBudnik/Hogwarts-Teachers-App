package com.bdev.hengschoolteacher.ui.views.branded

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.Toast
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.person.PersonContact
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.PhoneUtils
import kotlinx.android.synthetic.main.view_branded_phone.view.*
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_branded_phone)
open class BrandedPhoneView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(personContact: PersonContact) : BrandedPhoneView {
        brandedPhoneNameView.text = personContact.name
        brandedPhoneValueView.text = personContact.value

        setOnClickListener {
            PhoneUtils.call(context as BaseActivity, personContact.value)
        }

        setOnLongClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(personContact.value, personContact.value)

            clipboard.setPrimaryClip(clip)

            Toast.makeText(context, "Телефон скопирован", Toast.LENGTH_SHORT).show()

            return@setOnLongClickListener true
        }

        return this
    }
}
