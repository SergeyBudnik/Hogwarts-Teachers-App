package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import java.io.Serializable

class LessonStatusModel : Serializable {
    var lessonsStatuses: MutableList<LessonStatus> = ArrayList()
}

@EBean(scope = EBean.Scope.Singleton)
open class LessonStatusDao : CommonDao<LessonStatusModel>() {
    @RootContext
    lateinit var aContext: Context

    fun setLessonsStatuses(lessonsStatuses: List<LessonStatus>) {
        inTransaction {
            getValue().lessonsStatuses = lessonsStatuses.toMutableList()
        }
    }

    fun addLessonStatus(lessonStatus: LessonStatus) {
        inTransaction {
            getValue().lessonsStatuses.add(lessonStatus)
        }
    }

    fun getLessonsStatuses(): List<LessonStatus> {
        readCache()

        return getValue().lessonsStatuses
    }

    override fun getContext(): Context {
        return aContext
    }

    override fun getFileName(): String {
        return LessonStatusDao::class.java.canonicalName
    }

    override fun newInstance(): LessonStatusModel {
        return LessonStatusModel()
    }
}
