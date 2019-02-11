package com.bdev.hengschoolteacher.ui.utils

import android.content.Intent
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import java.io.Serializable

class RedirectUtils private constructor(private val current: BaseActivity) {
    private lateinit var target: Class<out BaseActivity>

    private var enterAnim = 0
    private var exitAnim = 0

    private val extra = HashMap<String, Serializable>()

    companion object {
        fun redirect(current: BaseActivity): RedirectUtils {
            return RedirectUtils(current)
        }
    }

    fun to(target: Class<out BaseActivity>): RedirectUtils {
        this.target = target

        return this
    }

    fun withAnim(enterAnim: Int, exitAnim: Int): RedirectUtils {
        this.enterAnim = enterAnim
        this.exitAnim = exitAnim

        return this
    }

    fun withExtra(key: String, value: Serializable): RedirectUtils {
        extra[key] = value

        return this
    }

    fun go() {
        execute(false, null)
    }

    fun goAndCloseCurrent() {
        execute(true, null)
    }

    fun goForResult(requestCode: Int) {
        execute(false, requestCode)
    }

    private fun execute(closeCurrent: Boolean, requestCode: Int?) {
        val intent = Intent(current, target)

        extra.forEach { intent.putExtra(it.key, it.value) }

        if (requestCode != null) {
            current.startActivityForResult(intent, requestCode)
        } else {
            current.startActivity(intent)
        }

        if (closeCurrent) {
            current.finish()
        }

        current.overridePendingTransition(enterAnim, exitAnim)
    }
}
