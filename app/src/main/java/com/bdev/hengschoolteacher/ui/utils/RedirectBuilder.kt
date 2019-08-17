package com.bdev.hengschoolteacher.ui.utils

import android.content.Intent
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import java.io.Serializable

private enum class RedirectFinishAction {
    DO_NOT_FINISH, CLOSE_CURRENT, CLOSE_ALL
}

class RedirectBuilder private constructor(private val current: BaseActivity) {
    private lateinit var target: Class<out BaseActivity>

    private var enterAnim = 0
    private var exitAnim = 0

    private val extra = HashMap<String, Serializable>()

    companion object {
        fun redirect(current: BaseActivity): RedirectBuilder {
            return RedirectBuilder(current)
        }
    }

    fun to(target: Class<out BaseActivity>): RedirectBuilder {
        this.target = target

        return this
    }

    fun withAnim(enterAnim: Int, exitAnim: Int): RedirectBuilder {
        this.enterAnim = enterAnim
        this.exitAnim = exitAnim

        return this
    }

    fun withExtra(key: String, value: Serializable): RedirectBuilder {
        extra[key] = value

        return this
    }

    fun go() {
        execute(redirectFinishAction = RedirectFinishAction.DO_NOT_FINISH, requestCode = null)
    }

    fun goAndCloseCurrent() {
        execute(redirectFinishAction = RedirectFinishAction.CLOSE_CURRENT, requestCode = null)
    }

    fun goAndCloseAll() {
        execute(redirectFinishAction = RedirectFinishAction.CLOSE_ALL, requestCode = null)
    }

    fun goForResult(requestCode: Int) {
        execute(redirectFinishAction = RedirectFinishAction.DO_NOT_FINISH, requestCode = requestCode)
    }

    private fun execute(redirectFinishAction: RedirectFinishAction, requestCode: Int?) {
        val intent = Intent(current, target)

        extra.forEach { intent.putExtra(it.key, it.value) }

        if (requestCode != null) {
            current.startActivityForResult(intent, requestCode)
        } else {
            current.startActivity(intent)
        }

        when (redirectFinishAction) {
            RedirectFinishAction.DO_NOT_FINISH -> { /* Do nothing */ }
            RedirectFinishAction.CLOSE_CURRENT -> { current.finish() }
            RedirectFinishAction.CLOSE_ALL -> { current.finishAffinity() }
        }

        current.overridePendingTransition(enterAnim, exitAnim)
    }
}
