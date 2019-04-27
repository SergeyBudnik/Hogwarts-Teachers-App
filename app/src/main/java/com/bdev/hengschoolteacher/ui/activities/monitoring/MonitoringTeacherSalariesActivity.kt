package com.bdev.hengschoolteacher.ui.activities.monitoring

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_monitoring_teacher_salary.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_teacher_salary)
open class MonitoringTeacherSalaryActivity : BaseActivity() {
    companion object {
        const val EXTRA_TEACHER_ID = "EXTRA_TEACHER_ID"
    }

    @Extra(EXTRA_TEACHER_ID)
    @JvmField
    var teacherId = 0L

    @AfterViews
    fun init() {
        monitoringTeacherSalaryHeaderView.setLeftButtonAction { doFinish() }

        monitoringTeacherSalarySecondaryHeaderView.bind(teacherId)
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
