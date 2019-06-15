package com.bdev.hengschoolteacher.ui.views.app.profile

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.profile.ProfileLessonsActivity_
import com.bdev.hengschoolteacher.ui.activities.profile.ProfileSalaryActivity_
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder.Companion.redirect
import kotlinx.android.synthetic.main.view_profile_header.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_profile_header)
open class ProfileHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private enum class Item(val id: Int) {
        LESSONS(1), SALARY(2);

        companion object {
            fun findById(id: Int): Item {
                return values().find { it.id == id } ?: throw RuntimeException()
            }
        }
    }

    private val item: Item

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ProfileHeaderView, 0, 0)

        try {
            item = Item.findById(ta.getInt(R.styleable.ProfileHeaderView_profile_item, 1))
        } finally {
            ta.recycle()
        }
    }

    @AfterViews
    fun init() {
        profileHeaderMyLessonsView.setActive(item == Item.LESSONS)
        profileHeaderMySalaryView.setActive(item == Item.SALARY)

        profileHeaderMyLessonsView.setOnClickListener {
            redirect(context as BaseActivity).to(ProfileLessonsActivity_::class.java).goAndCloseCurrent()
        }

        profileHeaderMySalaryView.setOnClickListener {
            redirect(context as BaseActivity).to(ProfileSalaryActivity_::class.java).goAndCloseCurrent()
        }
    }
}

