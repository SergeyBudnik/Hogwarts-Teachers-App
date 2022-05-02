package com.bdev.hengschoolteacher.ui.views.app.root

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.person.Person
import com.bdev.hengschoolteacher.data.school.person.PersonContacts
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import com.bdev.hengschoolteacher.ui.fragments.app_menu.views.AppMenuView
import com.bdev.hengschoolteacher.ui.views.branded.BrandedPopupView
import kotlinx.android.synthetic.main.view_app_layout.view.*

class HtPageRootView : DrawerLayout {
    private var customView: Pair<View, ViewGroup.LayoutParams?>? = null

    init {
        View.inflate(context, R.layout.view_app_layout, this)

        appLayoutContainerView.setScrimColor(Color.TRANSPARENT)

        if (!isInEditMode) {
//            appLayoutContainerView.addDrawerListener(
//                    object : ActionBarDrawerToggle(context as BasePageFragment, appLayoutContainerView, 0, 0) {
//                        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
//                            super.onDrawerSlide(drawerView, slideOffset)
//
//                            appLayoutContentView.translationX = drawerView.width * slideOffset
//                        }
//                    }
//            )
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun getPopupView(): BrandedPopupView {
        return appLayoutPopupView
    }

    fun openMenu() {
        appLayoutContainerView.openDrawer(GravityCompat.START)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        customView?.let { customView ->
            if (customView.second != null) {
                appLayoutEmbeddedContentView.addView(customView.first, customView.second)
            } else {
                appLayoutEmbeddedContentView.addView(customView.first)
            }
        }
    }

    override fun addView(child: View) {
        doAddView(child, null)
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        doAddView(child, params)
    }

    private fun doAddView(child: View, params: ViewGroup.LayoutParams?) {
        if (isCustomView(child)) {
            if (customView == null) {
                customView = Pair(child, params)
            } else {
                throw RuntimeException("Container can have only a single view")
            }
        } else {
            if (params != null) {
                super.addView(child, params)
            } else {
                super.addView(child)
            }
        }
    }

    private fun isCustomView(view: View): Boolean {
        return !listOf(
                R.id.appLayoutContainerView,
                R.id.appLayoutContentView,
                appLayoutEmbeddedContentView,
                R.id.appMenuFragment,
                R.id.appLayoutPopupView
        ).contains(view.id)
    }
}
