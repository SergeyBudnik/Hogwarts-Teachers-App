package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.student.Student
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StudentsModel constructor(
    @SerializedName("students") val students: List<Student>
)

interface StudentsDao : CommonDao<StudentsModel>

class StudentsDaoImpl @Inject constructor(
    @ApplicationContext context: Context
): StudentsDao, CommonDaoImpl<StudentsModel>(context = context) {
    override fun getFileName() = "students.data"
    override fun newInstance(): StudentsModel = StudentsModel(emptyList())
    override fun deserialize(json: String): StudentsModel = Gson().fromJson(json, StudentsModel::class.java)
}
