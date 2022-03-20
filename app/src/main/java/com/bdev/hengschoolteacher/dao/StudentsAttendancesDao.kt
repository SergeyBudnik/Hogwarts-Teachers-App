package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import dagger.hilt.android.qualifiers.ApplicationContext
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper
import javax.inject.Inject

class StudentsAttendancesModel @JsonCreator constructor(
    @JsonProperty("studentsAttendances") val studentsAttendances: List<StudentAttendance>
)

interface StudentsAttendancesDao : CommonDao<StudentsAttendancesModel>

class StudentsAttendancesDaoImpl @Inject constructor(
    @ApplicationContext context: Context
): StudentsAttendancesDao, CommonDaoImpl<StudentsAttendancesModel>(context = context) {
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
