package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper
import javax.inject.Inject

class LessonStatusModel @JsonCreator constructor(
    @JsonProperty("lessonsStatuses") val lessonsStatuses: List<LessonStatus>
)

interface LessonStatusDao : CommonDao<LessonStatusModel>

class LessonStatusDaoImpl @Inject constructor(
    @ApplicationContext context: Context
): LessonStatusDao, CommonDaoImpl<LessonStatusModel>(context = context) {
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
