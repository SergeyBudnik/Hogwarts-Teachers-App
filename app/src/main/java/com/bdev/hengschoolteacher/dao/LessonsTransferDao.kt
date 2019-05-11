package com.bdev.hengschoolteacher.dao

import com.bdev.hengschoolteacher.data.school.lesson.LessonTransfer
import org.androidannotations.annotations.EBean
import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.type.TypeReference

@EBean(scope = EBean.Scope.Singleton)
open class LessonsTransferDao : CommonDao<List<LessonTransfer>>() {
    override fun getFileName(): String {
        return "lessons-transfer.data"
    }

    override fun newInstance(): List<LessonTransfer> {
        return emptyList()
    }

    override fun deserialize(json: String): List<LessonTransfer> {
        return ObjectMapper().readValue(json, object: TypeReference<List<LessonTransfer>>() {})
    }
}
