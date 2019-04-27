package com.bdev.hengschoolteacher.dao

import com.bdev.hengschoolteacher.data.school.teacher.Teacher
import org.androidannotations.annotations.EBean
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper

class TeachersModel @JsonCreator constructor(
    @JsonProperty("teachers") val teachers: List<Teacher>
)

@EBean(scope = EBean.Scope.Singleton)
open class TeachersDao : CommonDao<TeachersModel>() {
    override fun getFileName(): String {
        return "teachers.data"
    }

    override fun newInstance(): TeachersModel {
        return TeachersModel(emptyList())
    }

    override fun deserialize(json: String): TeachersModel {
        return ObjectMapper().readValue(json, TeachersModel::class.java)
    }
}
