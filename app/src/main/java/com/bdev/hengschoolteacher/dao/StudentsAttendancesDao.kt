package com.bdev.hengschoolteacher.dao

import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import org.androidannotations.annotations.EBean
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper

class StudentsAttendancesModel @JsonCreator constructor(
    @JsonProperty("studentsAttendances") val studentsAttendances: List<StudentAttendance>
)

interface StudentsAttendancesDao : CommonDao<StudentsAttendancesModel>

@EBean(scope = EBean.Scope.Singleton)
open class StudentsAttendancesDaoImpl : StudentsAttendancesDao, CommonDaoImpl<StudentsAttendancesModel>() {
    override fun getFileName(): String {
        return "students-attendances.data"
    }

    override fun newInstance(): StudentsAttendancesModel {
        return StudentsAttendancesModel(emptyList())
    }

    override fun deserialize(json: String): StudentsAttendancesModel {
        return ObjectMapper().readValue(json, StudentsAttendancesModel::class.java)
    }
}
