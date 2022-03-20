package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.student.Student
import dagger.hilt.android.qualifiers.ApplicationContext
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper
import javax.inject.Inject

class StudentsModel @JsonCreator constructor(
    @JsonProperty("students") val students: List<Student>
)

interface StudentsDao : CommonDao<StudentsModel>

class StudentsDaoImpl @Inject constructor(
    @ApplicationContext context: Context
): StudentsDao, CommonDaoImpl<StudentsModel>(context = context) {
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
