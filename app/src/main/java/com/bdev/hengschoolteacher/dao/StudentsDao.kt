package com.bdev.hengschoolteacher.dao

import com.bdev.hengschoolteacher.data.school.student.Student
import org.androidannotations.annotations.EBean
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper

class StudentsModel @JsonCreator constructor(
    @JsonProperty("students") val students: List<Student>
)

@EBean(scope = EBean.Scope.Singleton)
open class StudentsDao : CommonDao<StudentsModel>() {
    override fun getFileName(): String {
        return "students.data"
    }

    override fun newInstance(): StudentsModel {
        return StudentsModel(emptyList())
    }

    override fun deserialize(json: String): StudentsModel {
        return ObjectMapper().readValue(json, StudentsModel::class.java)
    }
}
