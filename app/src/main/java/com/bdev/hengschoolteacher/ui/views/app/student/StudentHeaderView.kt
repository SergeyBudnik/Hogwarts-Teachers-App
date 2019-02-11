package com.bdev.hengschoolteacher.ui.views.app.student

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.student.StudentInformationActivity
import com.bdev.hengschoolteacher.ui.activities.student.StudentInformationActivity_
import com.bdev.hengschoolteacher.ui.activities.student.StudentPaymentActivity_
import com.bdev.hengschoolteacher.ui.utils.RedirectUtils.Companion.redirect
import kotlinx.android.synthetic.main.view_student_header.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_student_header)
open class StudentHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private enum class Item(val id: Int) {
        INFORMATION(1), PAYMENT(2);

        companion object {
            fun findById(id: Int): Item {
                return values().find { it.id == id } ?: throw RuntimeException()
            }
        }
    }

    private val item: Item

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.StudentHeaderView, 0, 0)

        try {
            item = Item.findById(ta.getInt(R.styleable.StudentHeaderView_student_item, 1))
        } finally {
            ta.recycle()
        }
    }

    @AfterViews
    fun init() {
        studentInformationView.setActive(item == Item.INFORMATION)
        studentPaymentView.setActive(item == Item.PAYMENT)
    }

    fun bind(student: Student) {
        studentInformationView.setOnClickListener { redirect(context as BaseActivity)
                .to(StudentInformationActivity_::class.java)
                .withExtra(StudentInformationActivity.EXTRA_STUDENT_ID, student.id)
                .withAnim(0, 0)
                .goAndCloseCurrent()
        }

        studentPaymentView.setOnClickListener { redirect(context as BaseActivity)
                .to(StudentPaymentActivity_::class.java)
                .withExtra(StudentInformationActivity.EXTRA_STUDENT_ID, student.id)
                .withAnim(0, 0)
                .goAndCloseCurrent()
        }
    }
}
