package com.bdev.hengschoolteacher.dao

import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import org.androidannotations.annotations.EBean
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper

class LessonStatusModel @JsonCreator constructor(
    @JsonProperty("lessonsStatuses") val lessonsStatuses: List<LessonStatus>
)

@EBean(scope = EBean.Scope.Singleton)
open class LessonStatusDao : CommonDao<LessonStatusModel>() {
    override fun getFileName(): String {
        return "lessons-statuses.data"
    }

    override fun newInstance(): LessonStatusModel {
        return LessonStatusModel(emptyList())
    }

    override fun deserialize(json: String): LessonStatusModel {
        return ObjectMapper().readValue(json, LessonStatusModel::class.java)
    }
}
