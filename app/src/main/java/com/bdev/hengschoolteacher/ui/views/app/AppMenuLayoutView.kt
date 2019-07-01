package com.bdev.hengschoolteacher.ui.views.app

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import kotlinx.android.synthetic.main.view_app_menu_layout.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_app_menu_layout)
open class AppMenuLayoutView : DrawerLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private val customViews: MutableSet<View> = HashSet()
    private val customViewsWithParams: MutableSet<Pair<View, ViewGroup.LayoutParams>> = HashSet()

    @AfterViews
    fun init() {
        layoutContainerView.setScrimColor(Color.TRANSPARENT)

        if (!isInEditMode) {
            layoutContainerView.addDrawerListener(object : ActionBarDrawerToggle(context as BaseActivity, layoutContainerView, 0, 0) {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)

                    layoutContentView.translationX = drawerView.width * slideOffset
                }
            })
        }

        customViews.forEach { layoutContentView.addView(it) }
        customViewsWithParams.forEach { layoutContentView.addView(it.first, it.second) }
    }

    fun setCurrentMenuItem(item: AppMenuView.Item) {
        layoutMenuView.setItemSelected(item)
    }

    fun openMenu() {
        layoutContainerView.openDrawer(GravityCompat.START)
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
        return !listOf(R.id.layoutContainerView, R.id.layoutContentView, R.id.layoutMenuView).contains(view.id)
    }
}
