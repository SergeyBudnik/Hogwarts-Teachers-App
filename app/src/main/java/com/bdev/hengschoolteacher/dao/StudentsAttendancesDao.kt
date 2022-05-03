package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StudentsAttendancesModel constructor(
    @SerializedName("studentsAttendances") val studentsAttendances: List<StudentAttendance>
)

interface StudentsAttendancesDao : CommonDao<StudentsAttendancesModel>

class StudentsAttendancesDaoImpl @Inject constructor(
    @ApplicationContext context: Context
): StudentsAttendancesDao, CommonDaoImpl<StudentsAttendancesModel>(context = context) {
    override fun getFileName() = "students-attendances.data"
    override fun newInstance(): StudentsAttendancesModel = StudentsAttendancesModel(emptyList())
    override fun deserialize(json: String): StudentsAttendancesModel = Gson().fromJson(json, StudentsAttendancesModel::class.java)
}
